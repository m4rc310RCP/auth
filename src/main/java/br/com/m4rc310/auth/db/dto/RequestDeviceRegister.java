package br.com.m4rc310.auth.db.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.m4rc310.auth.graphql.MConst;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.Data;

@Data 
@JsonIgnoreProperties(ignoreUnknown = true)
@GraphQLType(name = MConst.DTO$request_device_register)
public class RequestDeviceRegister implements MConst {
	@GraphQLQuery(name = CODE$request)
	@JsonProperty("request_code")
	private String code;
	
	@GraphQLQuery(name = IDENTIFY$device)
	@JsonProperty("device_id")
	private String deviceId;
	
}
