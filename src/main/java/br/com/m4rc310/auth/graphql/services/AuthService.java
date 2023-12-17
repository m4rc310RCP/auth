package br.com.m4rc310.auth.graphql.services;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.reactivestreams.Publisher;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import br.com.m4rc310.auth.db.dto.RequestDeviceRegister;
import br.com.m4rc310.auth.db.models.User;
import br.com.m4rc310.auth.graphql.MConst;
import foundation.cmo.opensales.graphql.exceptions.MException;
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
import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
@Service
@GraphQLApi
@EnableCaching
public class AuthService extends MService implements MConst {

	/** The Constant FLUX_REGISTER_DEVICE. */
	private static final String FLUX_REGISTER_DEVICE = "flux_gegister_device";

	/**
	 * Test.
	 *
	 * @return the date
	 */
	@MDate
	// @MAuth(rolesRequired = "CLIENT")
	@GraphQLQuery(name = "test")
	public Date test() throws MException {
		//throw new MException(0, "Error Test");
		 return new Date();
	}

	/**
	 * Auth user.
	 *
	 * @param hash the hash
	 * @return the user
	 * @throws Exception the exception
	 */
//	@MAuth(rolesRequired = "CLIENT")
	@GraphQLMutation(name = MUTATION$auth_user)
	public User authUser(@GraphQLArgument(name = CODE$hash) String hash) throws Exception {

			hash = jwt.decrypt(hash);
			hash = hash.replace("\"", "");

			int i = hash.indexOf(":");
			String username = hash.substring(0, i);

//			Pattern pattern = Pattern.compile("\\[([a-fA-F0-9\\-]+)\\]");
//			Matcher matcher = pattern.matcher(username);
//
//			String key = "";
//
//			if (matcher.find()) {
//				key = matcher.group(1);
//			}
//
//			username = username.replace("[" + key + "]", "");
//			log.info(key);

			// String password = hash.substring(i + 1);
			// password = jwt.decrypt(password);

			// password = jwt.decrypt(password);

			log.info(passwordEncoder.encode("12345"));
			log.info("Password Valid: {}",
					passwordEncoder.matches("$2a$10$UlD315sXUJeuUkl3vnUV1.45FcFQm2f3m.b16dI3LIKw1LuwrQ4pa", "$2a$10$IkiT6NUdtcq6wohpi4xaBeWSmBGzJ87oxOys6i1YG5Srt8JCyWWwe"));

			User user = new User();
			user.setUsername(username);
			// user.setPassword(password);

			return user;
		
	}

//	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
//		String json = "{\"username\":\"client\",\"roles\":[\"CLIENT\"]}";
//		MUser user = new ObjectMapper().readValue(json, MUser.class);
//		log.info(user.getUsername());
//	}

	/**
	 * Gets the roles.
	 *
	 * @param user the user
	 * @return the roles
	 */
	@GraphQLQuery(name = LIST$roles)
	public String[] getRoles(@GraphQLContext User user) {
		return new String[] { "ADMIN" };
	}

	/**
	 * Gets the access token.
	 *
	 * @param user the user
	 * @return the access token
	 */
	@GraphQLQuery(name = CODE$access_token, description = DESC$code_access_token)
	public String getAccessToken(@GraphQLContext User user) {
		MUser u = new MUser();
		u.setUsername(user.getUsername());
		return jwt.generateToken(MUserDetails.from(u));
	}

	// =========================

	/**
	 * Request register device.
	 *
	 * @return the publisher
	 * @throws Exception
	 */
	@MAuth(rolesRequired = "ADMIN")
	@GraphQLSubscription(name = SUBSCRIPTION$device_register)
	public Publisher<RequestDeviceRegister> requestRegisterDevice()  {
		try {
			
		log.info(FLUX_REGISTER_DEVICE);
		String code = getRandomNumber(4);
		String id = String.format("%s_%s", FLUX_REGISTER_DEVICE, code);
		RequestDeviceRegister resp = getRequestDeviceRegister(code);

		return fluxService.publish(RequestDeviceRegister.class, id, resp);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the username register.
	 *
	 * @param rdr the rdr
	 * @return the username register
	 */
	@GraphQLQuery(name = NAME$username)
	public String getUsernameRegister(@GraphQLContext RequestDeviceRegister rdr) {
		try {
			User user = rdr.getUserRegister();
			return user.getUsername();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Confirm request device register.
	 *
	 * @param code the code
	 * @return the request device register
	 * @throws Exception the exception
	 */
	@GraphQLMutation(name = MUTATION$confirm_register_device)
	public RequestDeviceRegister confirmRequestDeviceRegister(@GraphQLArgument(name = CODE$request) String code)
			throws Exception {

		String id = String.format("%s_%s", FLUX_REGISTER_DEVICE, code);

		if (fluxService.inPublish(RequestDeviceRegister.class, id)) {
			RequestDeviceRegister register = getRequestDeviceRegister(code);

			String uuid = UUID.randomUUID().toString();
			register.setDeviceId(uuid);

			fluxService.callPublish(RequestDeviceRegister.class, id, register);
			resetCache(code);
			return register;
		}

		throw getException(ERROR$invalid_code_register, code);
	}

	/**
	 * Gets the request device register.
	 *
	 * @param code the code
	 * @return the request device register
	 * @throws Exception
	 */
	@Cacheable(value = "device_request", key = "#code")
	@Transactional
	private RequestDeviceRegister getRequestDeviceRegister(String code) throws Exception {
		RequestDeviceRegister resp = new RequestDeviceRegister();
		resp.setCode(code);
		resp.setUserRegister(userAuth());
		return resp;
	}

	/**
	 * Reset cache.
	 *
	 * @param code the code
	 */
	@CacheEvict(value = "device_request", key = "#code")
	private void resetCache(String code) {}

	/**
	 * Gets the random number.
	 *
	 * @param length the length
	 * @return the random number
	 */
	private String getRandomNumber(int length) {
		return new Random().ints(0, 10).limit(length).mapToObj(Integer::toString).collect(Collectors.joining());
	}

	// =========================

}
