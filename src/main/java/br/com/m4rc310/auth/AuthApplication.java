package br.com.m4rc310.auth;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@PropertySource(ignoreResourceNotFound = true, value = "classpath:/security.properties")
public class AuthApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}
	
	@Bean
	void init() {
	    TimeZone.setDefault(TimeZone.getTimeZone("GMT-3"));
	}
	
	@Bean
	PasswordEncoder bCryptPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	}

}
