package ru.vyukov.anotherliverefresh.autoconfigure;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.AntPathMatcher;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@ConfigurationProperties("liverefresh")
public class AnotherLiveRefreshProperties {

	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	/**
	 * Active Live Refresh on true
	 */
	@Setter
	@Getter
	private boolean enable;

	/**
	 * default:
	 * 
	 * <pre>
	 *   ignore-file-changes:
	 *      - "/&#42;&#42;/application.properties"
	 *      - "/&#42;&#42;/application.yml"
	 *      - "/&#42;&#42;/git.properties"
	 * </pre>
	 */
	@NonNull
	@Getter
	@Setter
	private List<String> ignoreFileChanges = Arrays.asList("/**/application.properties", "/**/application.yml",
			"/**/git.properties");

	public boolean isIgnorePath(Path path) {
		for (String pattern : ignoreFileChanges) {
			if (antPathMatcher.match(pattern, path.toString())) {
				return true;
			}
		}
		return false;
	}

}