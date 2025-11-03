package com.Eureka_ClientB.Eureka_ClientB.FarewellController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.Eureka_ClientB.Eureka_ClientB.messaging.RabbitFarewellProducer;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/farewell")
public class FarewellController {
	
	private final RestTemplate restTemplate;
	private final RabbitFarewellProducer producer;
	
	public FarewellController(RestTemplate restTemplate, RabbitFarewellProducer producer) {
		this.restTemplate = restTemplate;
		this.producer = producer;
	}
	
	@GetMapping("/{name}")
	public String homePage(@PathVariable String name) {
		producer.sendFarewell(name);
		
		return "This is the Farewell HomePage, | Queued farewell for: " + name;
	}
	
	
	@GetMapping("/bye/{name}")
	@Operation(summary = "Says adieu and fetches greeting message")
	public String bye(@PathVariable String name) {
		String greeting = restTemplate.getForObject("http://greeting-service/greet/{name}", String.class, name);
		return "Goodbye " + name + " *clap" + greeting;
	}
}
