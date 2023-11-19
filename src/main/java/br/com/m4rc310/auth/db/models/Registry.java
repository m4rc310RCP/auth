package br.com.m4rc310.auth.db.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.m4rc310.auth.graphql.MConst;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.Data;

@Data
@Entity(name="${type.registry}")
@GraphQLType(name = "${type.registry}")
public class Registry implements Serializable, MConst{

	private static final long serialVersionUID = 4190021344891124755L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "${code.registry}")
	@GraphQLQuery(name = "${code.registry}")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = NAME$username)
	private User user;
	
	@Column(name = "${date.transaction}")
	@GraphQLQuery(name = "${date.transaction}")
	private Date dateTransaction;
}
