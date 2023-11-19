package br.com.m4rc310.auth.db.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.m4rc310.auth.graphql.MConst;
import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.Data;

@Data
@Entity(name = MConst.TYPE$auth_user)
@GraphQLType(name = MConst.TYPE$auth_user, description = MConst.DESC$type_auth_user)
public class User implements Serializable, MConst {
	
	private static final long serialVersionUID = 1568396898330354858L;

	@Id
	@Column(name = NAME$username)
	@GraphQLQuery(name = NAME$username, description = DESC$name_username)
	private String username;
	
	@Column(name = DESCRIPTION$password)
	@GraphQLIgnore
	private String password;
}
