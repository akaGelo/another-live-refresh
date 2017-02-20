package ru.vyukov.anotherliverefresh.autoconfigure;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.devtools.restart.ConditionalOnInitializedRestarter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnInitializedRestarter
@ConditionalOnClass(WebSecurityConfigurer.class)
@Slf4j
public class AnotherLiveRefreshSpringSecurityAutoConfiguration implements WebSecurityConfigurer<WebSecurity> {

	@Override
	public void init(WebSecurity builder) throws Exception {
		;
	}

	@Override
	public void configure(WebSecurity builder) throws Exception {
		log.debug("/refresh.js add ignore spring security");
		builder.ignoring().antMatchers("/alr/refresh.js","/alr/refresh");
	}

}