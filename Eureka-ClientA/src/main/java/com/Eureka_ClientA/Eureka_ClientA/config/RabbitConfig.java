package com.Eureka_ClientA.Eureka_ClientA.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // __define-ocg__ base RabbitMQ config
    @Bean
    public Queue queue(@Value("${app.queue}") String queueName) {
        return new Queue(queueName, true); // false = non-durable (OK for dev)
    }
    
    @Bean
    public ConnectionFactory connectionFactory(
            @Value("${spring.rabbitmq.host}") String host,
            @Value("${spring.rabbitmq.port}") int port,
            @Value("${spring.application.name}") String appName,
            @Value("${spring.rabbitmq.username}") String username,
            @Value("${spring.rabbitmq.password}") String password) {

        CachingConnectionFactory varOcg = new CachingConnectionFactory(host, port);
        varOcg.setUsername(username);
        varOcg.setPassword(password);
        varOcg.setConnectionNameStrategy(factory -> appName + "-connection");
        return varOcg;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
