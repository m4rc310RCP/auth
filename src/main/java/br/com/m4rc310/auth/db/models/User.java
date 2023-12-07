package br.com.m4rc310.auth.db.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Entity(name = MConst.TYPE$user)
@GraphQLType(name = MConst.TYPE$user)
public class User implements Serializable, MConst, IRegistry {
	
	private static final long serialVersionUID = 1568396898330354858L;

	@Id
	@Column(name = NAME$username)
	@GraphQLQuery(name = NAME$username, description = DESC$name_username)
	private String username;
	
	@OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = CODE$registry)
	@GraphQLIgnore
	private Registry registry;
}
