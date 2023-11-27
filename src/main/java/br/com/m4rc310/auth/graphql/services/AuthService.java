package br.com.m4rc310.auth.graphql.services;

import java.util.Date;
import java.util.Random;
import java.util.stream.Collectors;

import org.reactivestreams.Publisher;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
	
	//=========================
	
	@GraphQLSubscription(name = SUBSCRIPTION$device_register)
	public Publisher<RequestDeviceRegister> requestRegisterDevice() {
		String code = getRandomNumber(5);
		String id = String.format("%s_%s", FLUX_REGISTER_DEVICE, code);
		RequestDeviceRegister resp = getRequestDeviceRegister(code);
		return fluxService.publish(RequestDeviceRegister.class, id, resp);
	}
	
	@GraphQLQuery(name = NAME$username)
	public String getUsernameRegister(@GraphQLContext RequestDeviceRegister rdr) {
		try {
			User user = rdr.getUserRegister();
			return user.getUsername();			
		} catch (Exception e) {
			return "";
		}
	}
	
	@GraphQLMutation(name = MUTATION$confirm_register_device)
	public RequestDeviceRegister confirmRequestDeviceRegister(@GraphQLArgument(name = CODE$request) String code) throws Exception {
		
		String id = String.format("%s_%s", FLUX_REGISTER_DEVICE, code);
		
		if (fluxService.inPublish(RequestDeviceRegister.class, id)) {
			RequestDeviceRegister register = getRequestDeviceRegister(code);
			fluxService.callPublish(RequestDeviceRegister.class, id, register);
			resetCache(code);
			return register;
		}
		
		return null;
	}
	
	@Cacheable(value = "device_request", key = "#code")
	private RequestDeviceRegister getRequestDeviceRegister(String code) {
		RequestDeviceRegister resp = new RequestDeviceRegister();
		resp.setCode(code);
		resp.setUserRegister(userAuth());
		return resp;
	}
	
	@CacheEvict(value = "device_request", key = "#code")
	private void resetCache(String code) {}
	
	
	private String getRandomNumber(int length) {
        return new Random()
                .ints(0, 10) 
                .limit(length) 
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
    }
	
	//=========================
	
}
