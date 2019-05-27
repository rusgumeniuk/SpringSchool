package com.example;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@RefreshScope
@Configuration
public class EurekaClientLab2Application {
    @Value("${queue.group.name}")
    private String qGroup;

    @Value("${queue.student.name}")
    private String qStudent;

    @Value("${spring.rabbitmq.host}")
    private String brokerUrl;

    @Value("${topic.exchange.name}")
    private String topicName;

    @Value("${spring.rabbitmq.username}")
    private String user;

    @Value("${spring.rabbitmq.password}")
    private String pwd;

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientLab2Application.class, args);
	}

    @Bean(name ="queueGroup")
    public Queue queueGroup() {
        return new Queue(qGroup, true);
    }

    @Bean(name="exchangeGroup")
    public TopicExchange exchangeGroup() {
        return new TopicExchange(topicName);
    }

    @Bean(name="bindingGroup")
    public Binding bindingCustomer(Queue queueGroup, TopicExchange exchangeGroup) {
        return BindingBuilder.bind(queueGroup).to(exchangeGroup).with(qGroup);
    }

    @Bean(name="queueStudent")
    public Queue queueStudent() {
        return new Queue(qStudent, true);
    }

    @Bean(name="exchangeStudent")
    public TopicExchange exchangeStudent() {
        return new TopicExchange(topicName);
    }

    @Bean(name="bindingStudent")
    public Binding bindingShop(Queue queueStudent, TopicExchange exchangeStudent) {
        return BindingBuilder.bind(queueStudent).to(exchangeStudent).with(qStudent);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(brokerUrl);
        connectionFactory.setUsername(user);
        connectionFactory.setPassword(pwd);

        return connectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="rabbitTemplateGroup")
    @Primary
    public RabbitTemplate rabbitTemplateGroup() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(qGroup);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="rabbitTemplateStudent")
    public RabbitTemplate rabbitTemplateStudent() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(qStudent);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
