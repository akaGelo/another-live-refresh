package ru.vyukov.anotherliverefresh.autoconfigure;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import ru.vyukov.anotherliverefresh.filewatch.FileChangeListenerService;
import ru.vyukov.anotherliverefresh.filewatch.FileChangeListenerServiceImpl;
import ru.vyukov.anotherliverefresh.wrap.ResponseWrappedDispatcherServlet;
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
@DependsOn(value = "error")
@AutoConfigureBefore(DispatcherServletAutoConfiguration.class)
public class AnotherLiveRefreshAutoConfiguration implements WebSocketConfigurer {


	@Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
	public DispatcherServlet dispatcherServlet() {
		return new ResponseWrappedDispatcherServlet();
	}

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

}
