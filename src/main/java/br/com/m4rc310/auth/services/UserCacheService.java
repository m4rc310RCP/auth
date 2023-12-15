package br.com.m4rc310.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.m4rc310.auth.configs.SecurityConfig;
import br.com.m4rc310.auth.db.models.User;
import br.com.m4rc310.auth.db.models.auth.AuthUser;
import br.com.m4rc310.auth.db.models.auth.AuthUserId;
import br.com.m4rc310.auth.db.repositories.AuthUserRepository;
import br.com.m4rc310.auth.db.repositories.UserRepository;
import br.com.m4rc310.auth.graphql.MConst;
import foundation.cmo.opensales.graphql.messages.i18n.M;
import foundation.cmo.opensales.graphql.security.MGraphQLJwtService;
import foundation.cmo.opensales.graphql.security.dto.MUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserCacheService implements MConst {

	public static final String CACHE_FIRST_USER_MODE = "cache.first.user.mode";
	public static final String CACHE_USER = "cache.user";
	public static final String CACHE_MUSER = "cache.muser";
	public static final String CACHE_AUTH_USER = "cache.auth.user";
	public static final String CACHE_HASH_USERNAME = "cache.hash.username";
	public static final String CACHE_AUTHENTICATE = "cache.authenticate";

	@Autowired
	private M m;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MGraphQLJwtService jwt;

	@Autowired
	private PasswordEncoder encoder;

	public UserCacheService(SecurityConfig securityConfig) {
		securityConfig.setUserCacheService(this);
	}

	@Transactional
	public void registerDefaultUser() {
		try {
			if (isFirstUserMode()) {
				User user = new User();
				user.setUsername("root");
				user = userRepository.save(user);

				AuthUser auth = new AuthUser();

				AuthUserId id = new AuthUserId();
				id.setUser(user);
				id.setSequence(1L);
				auth.setId(id);
				
				auth.setRoles("ADMIN");
				auth.setHash(encoder.encode("root"));

				String token = String.format("%s:%s", user.getUsername(), auth.getHash());
				token = jwt.encrypt(token);

				log.info(token);


				authUserRepository.save(auth);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Cacheable(CACHE_FIRST_USER_MODE)
	public boolean isFirstUserMode() {
		return userRepository.count() == 0;
	}

	@CacheEvict(CACHE_FIRST_USER_MODE)
	public void clearCacheFirstuserMode() {
	}

	@Transactional()
	public User getUserRoot() {
		User user = new User();
		user.setUsername("root");
		return userRepository.save(user);
	}

	@Cacheable(CACHE_USER)
	public User getUser(String username) throws Exception {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new Exception(m.getString(ERROR$user_not_found, username)));
	}

	@Cacheable(CACHE_MUSER)
	public MUser getMUser(String username) throws Exception {
		User user = getUser(username);
		MUser u = new MUser();
		u.setCode(1L);
		u.setUsername(user.getUsername());
		//u.setRoles(new String[] { "ADMIN" });
		return u;
	}

	@Cacheable(CACHE_AUTH_USER)
	public AuthUser getAuthUser(MUser muser) throws Exception {
		String username = muser.getUsername();
		return authUserRepository.findByIdUserUsername(username).orElseThrow(()->new Exception(m.getString(ERROR$user_not_found, username)));
	}
	
	@Cacheable(CACHE_AUTHENTICATE)
	public MUser authenticate(String username, String hash) throws Exception {
		String localHash = getHashForUsername(username);
		
		log.info(localHash);	
		log.info(hash);	
		boolean isvalidate = encoder.matches(localHash, hash);
		log.info(">>>> {}", isvalidate);
		
		
		
		if (encoder.matches(localHash, hash)) {
			return getMUser(username);
		}
		
		throw new Exception(m.getString(ERROR$access_unauthorized, username));
	}
	
	@Cacheable(CACHE_HASH_USERNAME)
	public String getHashForUsername(String username) throws Exception {
		MUser u = new MUser();
		u.setUsername(username);
		return getAuthUser(u).getHash();
	}
	
	
	
	
}
