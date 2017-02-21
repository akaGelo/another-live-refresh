package ru.vyukov.anotherliverefresh.autoconfigure;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import ru.vyukov.anotherliverefresh.filewatch.FileChangeListenerService;
import ru.vyukov.anotherliverefresh.filewatch.FileChangeListenerServiceImpl;
import ru.vyukov.anotherliverefresh.filter.LiveRefreshIncludeFilter;
import ru.vyukov.anotherliverefresh.ws.LiveRefreshConnectionHandler;

@Configuration
@EnableWebSocket
@EnableConfigurationProperties(AnotherLiveRefreshProperties.class)
public class AnotherLiveRefreshAutoConfiguration implements WebSocketConfigurer {

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
		registry.addHandler(liveRefreshwebSocketHandler(), "/alr/refresh");
	}

	@Bean
	public LiveRefreshConnectionHandler liveRefreshwebSocketHandler() {
		return new LiveRefreshConnectionHandler();
	}

	@Bean
	public FileChangeListenerService classPathChangeListenerService() throws IOException, URISyntaxException {
		URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();
		FileChangeListenerServiceImpl fileChangeListenerServiceImpl = new FileChangeListenerServiceImpl(
				Arrays.asList(urls));

		fileChangeListenerServiceImpl.addFileChangeListener(liveRefreshwebSocketHandler());

		return fileChangeListenerServiceImpl;
	}

}
