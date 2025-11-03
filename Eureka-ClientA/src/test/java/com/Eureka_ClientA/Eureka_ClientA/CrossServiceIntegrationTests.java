package com.Eureka_ClientA.Eureka_ClientA;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class CrossServiceIntegrationTests {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Test
	void greetingShouldReachFarewellService() {
		String name = "Bry";
		String response = restTemplate.getForObject("http://farewell-service/farewell/" + name, String.class);
		
		assertThat(response).isEqualTo("This is the Farewell HomePage, " + name);
	}
}
