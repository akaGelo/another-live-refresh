package ru.vyukov.anotherliverefresh.sampleapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("default")
public class DemoApplicationNotSpecifiedPropertyTest {

	public static final String SCRIPT_CODE = "<script src=\"/alr/refresh.js\">";

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testIncludeRefreshJs() {
		String htmlBody = restTemplate.getForObject("/", String.class);

		assertThat(htmlBody).contains(SCRIPT_CODE);
	}

	@Test
	public void testResources() {
		ResponseEntity<String> refreshJsEntity = restTemplate.getForEntity("/alr/refresh.js", String.class);
		assertEquals(HttpStatus.OK, refreshJsEntity.getStatusCode());

		ResponseEntity<String> helpHtmpEntity = restTemplate.getForEntity("/alr/help.html", String.class);
		assertEquals(HttpStatus.OK, helpHtmpEntity.getStatusCode());

		ResponseEntity<String> refreshWhEndpointEntity = restTemplate.getForEntity("/alr/refresh", String.class);
		assertEquals(HttpStatus.BAD_REQUEST, refreshWhEndpointEntity.getStatusCode());
		assertEquals("Can \"Upgrade\" only to \"WebSocket\".", refreshWhEndpointEntity.getBody());

	}
}
