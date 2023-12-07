package br.com.m4rc310.auth.db.models.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import br.com.m4rc310.auth.db.models.User;
import br.com.m4rc310.auth.graphql.MConst;
import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;

@Data
@Embeddable
public class AuthUserId implements Serializable, MConst {

	private static final long serialVersionUID = 1L;

	@OneToOne(optional = false)
	@JoinColumn(name = NAME$username)
	@GraphQLIgnore
	private User user;

	@Column(name = NUMBER$sequence)
	@GraphQLQuery(name = NUMBER$sequence)
	private Long sequence;
}
