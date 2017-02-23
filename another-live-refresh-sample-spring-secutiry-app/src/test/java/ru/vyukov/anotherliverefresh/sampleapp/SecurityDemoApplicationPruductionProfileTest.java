package ru.vyukov.anotherliverefresh.sampleapp;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.vyukov.anotherliverefresh.sampleapp.SecurityDemoApplicationNotSpecifiedPropertyTest.SCRIPT_CODE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("production")
public class SecurityDemoApplicationPruductionProfileTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testIncludeRefreshJs() {
		// success auth test
		HttpHeaders headers = SecurityDemoApplicationNotSpecifiedPropertyTest.getBasicAuthHeaders();
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange("/", HttpMethod.GET, request, String.class);

		String htmlBody = response.getBody();

		assertThat(htmlBody).doesNotContain(SCRIPT_CODE);
	}

}
