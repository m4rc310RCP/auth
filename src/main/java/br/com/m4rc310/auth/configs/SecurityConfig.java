package br.com.m4rc310.auth.configs;

import java.util.Arrays;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import br.com.m4rc310.auth.graphql.MConst;
import foundation.cmo.opensales.graphql.exceptions.MException;
import foundation.cmo.opensales.graphql.messages.i18n.M;
import foundation.cmo.opensales.graphql.security.IMAuthUserProvider;
import foundation.cmo.opensales.graphql.security.MAuthToken;
import foundation.cmo.opensales.graphql.security.MEnumToken;
import foundation.cmo.opensales.graphql.security.MGraphQLJwtService;
import foundation.cmo.opensales.graphql.security.dto.MUser;

/**
 * The Class SecurityConfig.
 */
@Configuration
@EnableCaching
public class SecurityConfig implements IMAuthUserProvider, MConst {
	
	//@Autowired(required = false)
	private M m;
	
	/**
	 * Auth user.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the m user
	 * @throws Exception the exception
	 */
	@Override
	public MUser authUser(String username, Object password) throws Exception {
		return null;
	}
	
	@Override
	public void setMessage(M m) {
		this.m = m;
	}

	/**
	 * Gets the user from username.
	 *
	 * @param username the username
	 * @return the user from username
	 */
	@Override
	@Cacheable(cacheNames = "username")
	public MUser getUserFromUsername(String username) {

		if ("mlsilva".equals(username)) {
			MUser user = new MUser();
			user.setCode(1L);
			user.setUsername(username);
			user.setRoles(new String[] { "CLIENT" });

			return user;
		}

		return null;
	}

	/**
	 * Load user.
	 *
	 * @param jwt   the jwt
	 * @param type  the type
	 * @param token the token
	 * @return the m user
	 * @throws MException the m exception
	 */
	@Override
	@Cacheable(cacheNames = "user", key = "#token")
	public MUser loadUser(MGraphQLJwtService jwt, MEnumToken type, String token) throws MException {
		String username;
		switch (type) {
		case TEST:
			int i = token.indexOf(":");
			String sid = token.substring(0, i);
			Long id = Long.parseLong(sid);
			String login = token.substring(i + 1);
			MUser user = new MUser();
			user.setRequestId(sid);
			user.setUsername(login);
			user.setCode(id);

			user.setRoles(new String[] { "ADMIN" });
			return user;
		case BASIC:
			try {
				user = jwt.userFromToken(token, MUser.class);
				user.setRoles(new String[] {"CLIENT"});
				if (!isValidUser(user)) {
					throw getWebException(402, ERROR$access_unauthorized);
				}
				return user;
			} catch (Exception e) {
				e.printStackTrace();
			}
		case BEARER:
			username = jwt.extractUsername(token);
			user = getUserFromUsername(username);
			if (!isValidUser(user)) {
				throw getWebException(402,  ERROR$access_unauthorized);
			}
			return user;
		default:
			break;
		}

		throw getWebException(401, ERROR$access_unauthorized);
	}
	
	private MException getWebException(int code, String message, Object... args) {
		if (m != null) {
			message = m.getString(message, args);			
		}
		return MException.get(code, message);
	} 

	/**
	 * Checks if is valid user.
	 *
	 * @param user the user
	 * @return true, if is valid user
	 */
	@Override
	public boolean isValidUser(MUser user) {
		return true;
	}

	/**
	 * Valid user access.
	 *
	 * @param authToken the auth token
	 * @param roles     the roles
	 * @throws MException the m exception
	 */
	@Override
	public void validUserAccess(MAuthToken authToken, String[] roles) throws MException {
		boolean isAuth = authToken == null ? false : authToken.getAuthorities().stream()
				.anyMatch(ga -> Arrays.asList(roles).contains(ga.getAuthority()));

		if (!isAuth) {
			throw getWebException(401, ERROR$access_unauthorized);
		}
	}

}
