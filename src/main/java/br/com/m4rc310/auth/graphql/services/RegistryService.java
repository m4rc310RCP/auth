package br.com.m4rc310.auth.graphql.services;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.com.m4rc310.auth.db.models.IRegistry;
import br.com.m4rc310.auth.db.models.User;
import foundation.cmo.opensales.graphql.mappers.annotations.MDate;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

/**
 * The Class RegistryService.
 */
@Service
@GraphQLApi
public class RegistryService extends MService {

	//-------------------------------------------------//
	@MDate
	@GraphQLQuery(name = DATE$transaction, description = DESC$date_transaction)
	public Date dateRegistry(@GraphQLContext IRegistry reg) {
		try {
			return reg.getRegistry().getDateTransaction();
		} catch (Exception e) {
			return null;
		}
	}
	
	@GraphQLQuery(name = NAME$username, description = DESC$name_username)
	public String usernameRegistry(@GraphQLContext IRegistry reg) {
		try {
			return reg.getRegistry().getUser().getUsername();
		} catch (Exception e) {
			return null;
		}
	}
	//-------------------------------------------------//
	@Transactional
	@GraphQLMutation(name = MUTATION$store_user, description = DESC$mutation_store_user)
	public User storeUser(@GraphQLArgument(name = FIELD$user, description = DESC$field_user) User user) throws Exception {
		try {			
			clone(userRepository, "findByUsername", "username", user);
			putRegistryInfo(user);
			return userRepository.save(user);
		} catch (Exception e) {
			throw e;
		}
	}
	
}
