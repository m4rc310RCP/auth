package br.com.m4rc310.auth.graphql.services;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.m4rc310.auth.graphql.MConst;
import br.com.m4rc310.auth.graphql.MEnumError;
import foundation.cmo.opensales.graphql.messages.i18n.M;
import foundation.cmo.opensales.graphql.security.MGraphQLJwtService;
import foundation.cmo.opensales.graphql.services.MFluxService;

public class MService implements MConst {
	
	@Autowired
	protected MFluxService fluxService;
	
	@Autowired
	protected MGraphQLJwtService jwt;
	
	@Autowired
	protected M m;
	
	protected String sa;
	
	protected <T> T fromJson(String json, Class<T> type) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, type);
	}
	
	protected Exception getException(String message, Object... args) {
		return new Exception(m.getString(message, args));
	}
	
	protected Exception getException(MEnumError error) {
		//return new Exception(m.getString(message, args));
		return new Exception();
	}

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

	
}
