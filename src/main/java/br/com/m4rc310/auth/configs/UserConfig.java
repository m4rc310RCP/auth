package br.com.m4rc310.auth.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import br.com.m4rc310.auth.graphql.MConst;
import br.com.m4rc310.auth.services.UserCacheService;

@Configuration
@EnableCaching
public class UserConfig implements MConst {
	
	@Autowired
	private UserCacheService userCacheService;

	@Bean
	@Transactional
	void initUserConfig() throws Exception {
		userCacheService.registerDefaultUser();
	}
}
