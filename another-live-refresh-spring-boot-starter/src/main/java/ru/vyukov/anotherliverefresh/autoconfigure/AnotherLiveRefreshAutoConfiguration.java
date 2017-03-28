package ru.vyukov.anotherliverefresh.autoconfigure;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.aop.support.RootClassFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.View;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import ru.vyukov.anotherliverefresh.filewatch.FileChangeListenerService;
import ru.vyukov.anotherliverefresh.filewatch.FileChangeListenerServiceImpl;
import ru.vyukov.anotherliverefresh.interceptor.RenderMethodInterceptor;
import ru.vyukov.anotherliverefresh.ws.LiveRefreshConnectionHandler;

/**
 * 
 * @author gelo
 *
 */
@Configuration
@EnableWebSocket
@ConditionalOnProperty(value = "liverefresh.enable", matchIfMissing = true)
@EnableConfigurationProperties(AnotherLiveRefreshProperties.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AnotherLiveRefreshAutoConfiguration implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(liveRefreshwebSocketHandler(), "/alr/refresh");
	}

	@Bean
	public LiveRefreshConnectionHandler liveRefreshwebSocketHandler() {
		return new LiveRefreshConnectionHandler();
	}

	@Bean
	public FileChangeListenerService classPathChangeListenerService(@Autowired AnotherLiveRefreshProperties properties)
			throws IOException, URISyntaxException {
		URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();

		FileChangeListenerServiceImpl fileChangeListenerServiceImpl = new FileChangeListenerServiceImpl(
				Arrays.asList(urls), properties);

		fileChangeListenerServiceImpl.addFileChangeListener(liveRefreshwebSocketHandler());
		return fileChangeListenerServiceImpl;
	}

	@Bean
	public DefaultPointcutAdvisor renderMethodInterceptorAdvisor() {
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.setClassFilter(new RootClassFilter(View.class));
		pointcut.setMappedName(RenderMethodInterceptor.METHOD_NAME);

		return new DefaultPointcutAdvisor(pointcut, new RenderMethodInterceptor());
	}

}
