package ru.vyukov.anotherliverefresh.sampleapp;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.vyukov.anotherliverefresh.sampleapp.DemoApplicationNotSpecifiedPropertyTest.SCRIPT_CODE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("production")
public class DemoApplicationPruductionProfileTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testIncludeRefreshJs() {
		String htmlBody = restTemplate.getForObject("/", String.class);

		assertThat(htmlBody).doesNotContain(SCRIPT_CODE);
	}

}
