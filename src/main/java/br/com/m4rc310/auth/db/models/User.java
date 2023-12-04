package br.com.m4rc310.auth.db.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import br.com.m4rc310.auth.graphql.MConst;
import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.Data;

/**
 * Instantiates a new user.
 */
@Data
@Entity(name = MConst.TYPE$auth_user)
@GraphQLType(name = MConst.TYPE$auth_user, description = MConst.DESC$type_auth_user)
public class User implements Serializable, MConst, IRegistry {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1568396898330354858L;

	/** The username. */
	@Id
	@Column(name = NAME$username)
	@GraphQLQuery(name = NAME$username, description = DESC$name_username)
	private String username;
	
	/** The password. */
	@Column(name = DESCRIPTION$password)
	@GraphQLIgnore
	private String password;
	
	/** The registry. */
	@OneToOne
	@JoinColumn(name = "${code.registry}")
	@GraphQLIgnore
	private Registry registry;
}
