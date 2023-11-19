package br.com.m4rc310.auth.configs;

import java.util.Arrays;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import br.com.m4rc310.auth.graphql.MConst;
import foundation.cmo.opensales.graphql.exceptions.MException;
import foundation.cmo.opensales.graphql.security.IMAuthUserProvider;
import foundation.cmo.opensales.graphql.security.MAuthToken;
import foundation.cmo.opensales.graphql.security.MEnumToken;
import foundation.cmo.opensales.graphql.security.MGraphQLJwtService;
import foundation.cmo.opensales.graphql.security.dto.MUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableCaching
public class SecurityConfig implements IMAuthUserProvider, MConst {

	@Override
	public MUser authUser(String username, Object password) throws Exception {
		log.info("auth: {}", username);
		return null;
	}

	@Override
	@Cacheable(cacheNames = "username")
	public MUser getUserFromUsername(String username) {

		log.info("Username: {}", username);
		if ("mlsilva".equals(username)) {
			MUser user = new MUser();
			user.setCode(1L);
			user.setUsername(username);
			user.setRoles(new String[] { "ADMIN" });

			return user;
		}

		return null;
	}

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
					throw MException.get(402, ERROR$access_unauthorized);
				}
				return user;
			} catch (Exception e) {
				e.printStackTrace();
			}
		case BEARER:
			username = jwt.extractUsername(token);
			user = getUserFromUsername(username);
			if (!isValidUser(user)) {
				throw MException.get(402,  ERROR$access_unauthorized);
			}
			return user;
		default:
			break;
		}

		throw MException.get(401, ERROR$access_unauthorized);
	}

	@Override
	public boolean isValidUser(MUser user) {
		log.info("Valid: {}", user);
		return true;
	}

	@Override
	public void validUserAccess(MAuthToken authToken, String[] roles) throws MException {
		boolean isAuth = authToken.getAuthorities().stream()
				.anyMatch(ga -> Arrays.asList(roles).contains(ga.getAuthority()));

		if (!isAuth) {
			throw MException.get(401, ERROR$access_unauthorized);
		}
	}

}
