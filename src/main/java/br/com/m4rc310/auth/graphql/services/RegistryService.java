package br.com.m4rc310.auth.graphql.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.m4rc310.auth.db.models.IRegistry;
import foundation.cmo.opensales.graphql.mappers.annotations.MDate;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

/**
 * The Class RegistryService.
 */
@Service
@GraphQLApi
public class RegistryService extends MService {

	/**
	 * Date registry.
	 *
	 * @param reg the reg
	 * @return the date
	 */
	@MDate
	@GraphQLQuery(name = "${date.transaction}")
	public Date dateRegistry(@GraphQLContext IRegistry reg) {
		try {
			return reg.getRegistry().getDateTransaction();
		} catch (Exception e) {
			return null;
		}
	}
	
	
}
