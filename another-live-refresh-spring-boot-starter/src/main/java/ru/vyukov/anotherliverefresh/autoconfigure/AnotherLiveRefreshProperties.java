package ru.vyukov.anotherliverefresh.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;


@Data
@ConfigurationProperties("liverefresh")
public class AnotherLiveRefreshProperties {

	
	private boolean enable = true;

}