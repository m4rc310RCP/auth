package br.com.m4rc310.auth.graphql.services;

import org.springframework.stereotype.Service;

import br.com.m4rc310.auth.graphql.MConst;
import foundation.cmo.opensales.graphql.security.MAuth;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

@Service
@GraphQLApi
public class AuthService implements MConst {
	
	@MAuth(rolesRequired = "CLIENT")
	@GraphQLQuery(name = "test")
	public String test() {
		return "OK";
	}
}
