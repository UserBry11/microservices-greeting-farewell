package com.Eureka_ClientA.Eureka_ClientA.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FarewellListener {
	private volatile String lastMessage = "No message received yet.";
	
	@RabbitListener(queues = "${app.queue}")
	public void receiveMessage(String payload) {
		System.out.println("[GreetingService] Received via RabbitMQ: " + payload);
		lastMessage = payload;
	}
	
	public String getLastMessage() {
		return lastMessage;
	}
	
}
