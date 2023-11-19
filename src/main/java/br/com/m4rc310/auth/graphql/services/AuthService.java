package br.com.m4rc310.auth.graphql.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.m4rc310.auth.graphql.MConst;
import foundation.cmo.opensales.graphql.mappers.annotations.MDate;
import foundation.cmo.opensales.graphql.security.MAuth;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

@Service
@GraphQLApi
public class AuthService implements MConst {
	
	@MDate
	@MAuth(rolesRequired = "CLIENT")
	@GraphQLQuery(name = "test")
	public Date test() {
		return new Date();
	}
	
}
