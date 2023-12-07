package br.com.m4rc310.auth.db.models.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import br.com.m4rc310.auth.graphql.MConst;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.Data;

@Data
@Entity(name = MConst.TYPE$auth_user)
@GraphQLType(name = MConst.TYPE$auth_user)
public class AuthUser implements Serializable, MConst {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private AuthUserId id;
	
	@Column(name = CODE$hash)
	@GraphQLQuery(name = CODE$hash, description = DESC$code_hash)
	private String hash;

	@Column(name = LIST$roles)
	@GraphQLQuery(name = LIST$roles, description = DESC$list_roles)
	private String roles;
}
