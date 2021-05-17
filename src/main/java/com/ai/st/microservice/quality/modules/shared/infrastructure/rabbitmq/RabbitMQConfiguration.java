package com.ai.st.microservice.quality.modules.shared.infrastructure.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Value("${st.rabbitmq.queueResultValidationProducts.queue}")
    public String queueResultValidation;

    @Value("${st.rabbitmq.queueResultValidationProducts.exchange}")
    public String exchangeResultValidation;

    @Value("${st.rabbitmq.queueResultValidationProducts.routingkey}")
    public String routingKeyResultValidation;

    @Bean
    public Queue QueueResultValidation() {
        return new Queue(queueResultValidation, false);
    }

    @Bean
    public DirectExchange exchangeResultValidation() {
        return new DirectExchange(exchangeResultValidation);
    }

    @Bean
    public Binding bindingQueueResultValidation() {
        return BindingBuilder.bind(QueueResultValidation()).to(exchangeResultValidation())
                .with(routingKeyResultValidation);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}
