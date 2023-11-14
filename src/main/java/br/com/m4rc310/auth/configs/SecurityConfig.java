package br.com.m4rc310.auth.configs;

import org.springframework.context.annotation.Configuration;

import foundation.cmo.opensales.graphql.security.IMAuthUserProvider;
import foundation.cmo.opensales.graphql.security.dto.MUser;

@Configuration
public class SecurityConfig implements IMAuthUserProvider {

	@Override
	public MUser authUser(String username, Object password) throws Exception {
		return null;
	}

	@Override
	public MUser getUserFromUsername(String username) {
		return null;
	}

	@Override
	public boolean isValidUser(MUser user) {
		return true;
	}

}
