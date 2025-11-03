package com.Eureka_ClientA.Eureka_ClientA.GreetingController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.Eureka_ClientA.Eureka_ClientA.messaging.FarewellListener;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/greet")
public class GreetingController {

	  private final RestTemplate restTemplate;
	  private final FarewellListener farewellListener;

	  public GreetingController(RestTemplate restTemplate, FarewellListener farewellListener) {
	    this.restTemplate = restTemplate;
	    this.farewellListener = farewellListener;
	  }
	  
	  @GetMapping("/{name}")
	  public String homePage(@PathVariable String name) {
		  return "This is the Greeting HomePage, " + name;
	  }
	  
	  @GetMapping("/lastMessage")
	  public String receiveMessage() {
		  String message = farewellListener.getLastMessage();
		  System.out.println(message);
		  return message;
	  }
	  

	  @GetMapping("/hello/{name}")
	  @Operation(summary = "Greets and fetches farewell message")
	  public String hello(@PathVariable String name) {
	    // Call across services using the Eureka service-id, not a hardcoded host/port
	    String farewell = restTemplate.getForObject(
	        "http://farewell-service/farewell/{name}", String.class, name);
	    return "Hello " + name + " 👋 — and " + farewell;
	  }
	}
