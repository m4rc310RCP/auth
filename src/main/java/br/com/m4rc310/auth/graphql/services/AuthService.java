package br.com.m4rc310.auth.graphql.services;

import java.util.Date;
import java.util.Random;
import java.util.stream.Collectors;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import br.com.m4rc310.auth.db.dto.RequestDeviceRegister;
import br.com.m4rc310.auth.db.models.User;
import br.com.m4rc310.auth.graphql.MConst;
import foundation.cmo.opensales.graphql.mappers.annotations.MDate;
import foundation.cmo.opensales.graphql.security.MAuth;
import foundation.cmo.opensales.graphql.security.dto.MUser;
import foundation.cmo.opensales.graphql.security.dto.MUserDetails;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLSubscription;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

@Service
@GraphQLApi
public class AuthService extends MService implements MConst {
	
	private static final String FLUX_REGISTER_DEVICE = "flux_gegister_device";
	
	@MDate
	@MAuth(rolesRequired = "CLIENT")
	@GraphQLQuery(name = "test")
	public Date test() {
		return new Date();
	}
	
//	@MAuth(rolesRequired = "CLIENT")
	@GraphQLMutation(name = MUTATION$auth_user)
	public User authUser(@GraphQLArgument(name = "${code.hash}") String hash) throws Exception {
		hash = jwt.decrypt(hash);
		hash = hash.replace("\"", "");
		
		int i = hash.indexOf(":");
		String username = hash.substring(0, i);
		String password = hash.substring(i+1);
		
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		
		return user;
	}
	
	@GraphQLQuery(name = CODE$access_token, description = DESC$code_access_token)
	public String getAccessToken(@GraphQLContext User user) {
		MUser u = new MUser();
		u.setUsername(user.getUsername());
		return jwt.generateToken(MUserDetails.from(u));
	}
	
	@GraphQLSubscription(name = SUBSCRIPTION$device_register)
	public Publisher<RequestDeviceRegister> requestRegisterDevice() {
		String code = getRandomNumber(5);
		String id = String.format("%s_%s", FLUX_REGISTER_DEVICE, code);
		
		RequestDeviceRegister resp = new RequestDeviceRegister();
		resp.setCode(code);
		
		return fluxService.publish(RequestDeviceRegister.class, id, resp);
	}
	
	private String getRandomNumber(int length) {
        return new Random()
                .ints(0, 10) 
                .limit(length) 
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
    }
	
	
}
