package com.Eureka_ClientB.Eureka_ClientB.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitFarewellProducer {
	private final RabbitTemplate rabbitTemplate;
	private final String queueName;
	
	public RabbitFarewellProducer(RabbitTemplate rabbitTemplate, @Value("${app.queue}") String queueName) {
		this.rabbitTemplate = rabbitTemplate;
		this.queueName = queueName;
	}
	
	public void sendFarewell(String name) {
		String msg = "Goodbye, " + name + " (from FarewellService)"; 
		rabbitTemplate.convertAndSend(queueName, msg);
		System.out.println("[FarewellService] Published to RabbitMQ: " + msg);
	}
}
