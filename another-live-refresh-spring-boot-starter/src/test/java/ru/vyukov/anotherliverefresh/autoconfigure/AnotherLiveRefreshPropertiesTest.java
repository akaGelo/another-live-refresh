package ru.vyukov.anotherliverefresh.autoconfigure;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class AnotherLiveRefreshPropertiesTest {

	private AnotherLiveRefreshProperties properties;

	@Before
	public void before() {
		properties = new AnotherLiveRefreshProperties();
	}

	@Test
	public void testIsIgnorePath() throws Exception {
		properties.setIgnoreFileChanges(Arrays.asList("/**/application.yml","startString.yml"));
		
		assertTrue(properties.isIgnorePath(Paths.get("/folder/app/application.yml")));
		assertFalse(properties.isIgnorePath(Paths.get("application.yml")));
		
		
		assertTrue(properties.isIgnorePath(Paths.get("startString.yml")));
		assertFalse(properties.isIgnorePath(Paths.get("/othreFolder/startString.yml")));
	}
	
	
	@Test
	
	public void testDefaultConfig(){
		assertTrue(properties.isIgnorePath(Paths.get("/folder/app/application.yml")));
		assertTrue(properties.isIgnorePath(Paths.get("/folder/app/application.properties")));
		assertTrue(properties.isIgnorePath(Paths.get("/folder/app/git.properties")));
		
	}

}
