package br.com.m4rc310.auth.graphql.services;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.m4rc310.auth.db.models.User;
import br.com.m4rc310.auth.graphql.MConst;
import br.com.m4rc310.auth.graphql.MEnumError;
import foundation.cmo.opensales.graphql.messages.i18n.M;
import foundation.cmo.opensales.graphql.security.MGraphQLJwtService;
import foundation.cmo.opensales.graphql.security.dto.MUser;
import foundation.cmo.opensales.graphql.services.MFluxService;

/**
 * The Class MService.
 */
public class MService implements MConst {
	
	/** The flux service. */
	@Autowired
	protected MFluxService fluxService;
	
	/** The jwt. */
	@Autowired
	protected MGraphQLJwtService jwt;
	
	/** The m. */
	@Autowired()
	protected M m;
	
	/** The password encoder. */
	@Autowired
	protected PasswordEncoder passwordEncoder;
	
	/** The sa. */
	protected String sa;
	
	/**
	 * From json.
	 *
	 * @param <T>  the generic type
	 * @param json the json
	 * @param type the type
	 * @return the t
	 * @throws JsonMappingException    the json mapping exception
	 * @throws JsonProcessingException the json processing exception
	 */
	protected <T> T fromJson(String json, Class<T> type) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, type);
	}
	
	/**
	 * Gets the exception.
	 *
	 * @param message the message
	 * @param args    the args
	 * @return the exception
	 */
	protected Exception getException(String message, Object... args) {
		return new Exception(m.getString(message, args));
	}
	
	/**
	 * Gets the exception.
	 *
	 * @param error the error
	 * @return the exception
	 */
	protected Exception getException(MEnumError error) {
		//return new Exception(m.getString(message, args));
		return new Exception();
	}

	/**
	 * Clone.
	 *
	 * @param <T>        the generic type
	 * @param repository the repository
	 * @param query      the query
	 * @param fieldId    the field id
	 * @param value      the value
	 * @throws Exception the exception
	 */
	protected <T> void clone(JpaRepository<T, ?> repository, String query, String fieldId, T value) throws Exception {
		if (Objects.nonNull(value)) {
			Class<?> typeValue = value.getClass();
			Field field = typeValue.getDeclaredField(fieldId);
			field.setAccessible(true);
			Object id = field.get(value);
			if (Objects.nonNull(id)) {
				Class<?> typeRepository = repository.getClass();
				Method method = typeRepository.getMethod(query, id.getClass());
				method.setAccessible(true);

				@SuppressWarnings("unchecked")
				Optional<T> result = (Optional<T>) method.invoke(repository, id);
				if (result.isPresent()) {
					T a = result.get();
					fluxService.cloneAtoB(a, value);
				}
			}
		}
	}

	/**
	 * User auth.
	 *
	 * @return the user
	 */
	protected User userAuth() {
		MUser authenticatedUser = fluxService.authenticatedUser();
		if (authenticatedUser==null) {
			return null;
		}
		
		User user = new User();
		user.setUsername(authenticatedUser.getUsername());	
		return user;
		}
	
}
