package ru.vyukov.anotherliverefresh.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.devtools.restart.ConditionalOnInitializedRestarter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@ConditionalOnInitializedRestarter
@EnableConfigurationProperties(AnotherLiveRefreshProperties.class)
public class AnotherLiveRefreshAutoConfiguration implements WebSocketConfigurer{

	@Autowired
	private AnotherLiveRefreshProperties properties; 

	@Bean
	public FilterRegistrationBean registerCorsFilter(LiveRefreshIncludeFilter filter) {
		FilterRegistrationBean reg = new FilterRegistrationBean(filter);
		return reg;
	}

	@Bean
	public LiveRefreshIncludeFilter liveRefreshIncludeFilter() {
		return new LiveRefreshIncludeFilter();
	}

	@Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/alr/refresh");
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new LiveRefreshConnectionHandler();
    }

}
