package com.Eureka_ClientA.Eureka_ClientA;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EurekaClientAApplicationTests {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	void contextLoads() {
		String name = "Bry";
		String url = "http://localhost:" + port + "/greet/" + name;
		
		String response = restTemplate.getForObject(url, String.class);
		
		assertThat(response).isEqualTo("This is the Greeting HomePage, " + name);
	}

	@Test
	void contextLoad2() {
		String name = "Fernandez";
		assertThat(name).isEqualTo("Fernandez");
//		String url = "http://farewell-service/farewell/{name}";
//		String response = restTemplate.getForObject(url, String.class, name);
//		assertThat(response).isEqualTo("This is the Farewell HomePage,  " + name);
	}

}
