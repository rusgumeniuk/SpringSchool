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
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@SpringBootApplication
@RefreshScope
@Configuration
public class EurekaClientLab2Application {
    @Value("${queue.studentGroupTeacher.name}")
    private String qStudentGroupTeacher;

    @Value("${queue.system.name}")
    private String qSystem;
    
    @Value("${queue.lesSub.name}")
    private String qLesSub;

    @Value("${queue.arch.name}")
    private String qArch;

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
	
    @Bean(name ="queueLesSub")
    public Queue queueLesSub() {
        return new Queue(qLesSub, true);
    }
    @Bean(name="queueSystem")
    public Queue queueSystem() {
        return new Queue(qSystem, true);
    }
    @Bean(name ="queueStudentGroupTeacher")
    public Queue queueStudentGroupTeacher() {
        return new Queue(qStudentGroupTeacher, true);
    }
    @Bean(name="queueArch")
    public Queue queueArch() {
        return new Queue(qArch, true);
    }

    @Bean(name="exchangeLesSub")
    public TopicExchange exchangeLesSub() {
        return new TopicExchange(topicName);
    }
    @Bean(name="exchangeSystem")
    public TopicExchange exchangeSystem() {
        return new TopicExchange(topicName);
    }
    @Bean(name="exchangeStudentGroupTeacher")
    public TopicExchange exchangeStudentGroupTeacher() {
        return new TopicExchange(topicName);
    }
    @Bean(name="exchangeArch")
    public TopicExchange exchangeArch() {
        return new TopicExchange(topicName);
    }

    @Bean(name="bindingLesSub")
    public Binding bindingLesSub(Queue queueLesSub, TopicExchange exchangeLesSub) {
        return BindingBuilder.bind(queueLesSub).to(exchangeLesSub).with(qLesSub);
    }
    @Bean(name="bindingSystem")
    public Binding bindingSystem(Queue queueSystem, TopicExchange exchangeSystem) {
        return BindingBuilder.bind(queueSystem).to(exchangeSystem).with(qSystem);
    }
    @Bean(name="bindingStudentGroupTeacher")
    public Binding bindingStudentGroupTeacher(Queue queueStudentGroupTeacher, TopicExchange exchangeStudentGroupTeacher) {
        return BindingBuilder.bind(queueStudentGroupTeacher).to(exchangeStudentGroupTeacher).with(qStudentGroupTeacher);
    }
    @Bean(name="bindingArch")
    public Binding bindingArch(Queue queueArch, TopicExchange exchangeArch) {
        return BindingBuilder.bind(queueArch).to(exchangeArch).with(qArch);
    }

    @Bean(name="rabbitTemplateStudentGroupTeacher")
    @Primary
    public RabbitTemplate rabbitTemplateStudentGroupTeacher() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(qStudentGroupTeacher);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
    @Bean(name="rabbitTemplateLesSub")
    public RabbitTemplate rabbitTemplateLesSub() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(qLesSub);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
    @Bean(name="rabbitTemplateSystem")
    public RabbitTemplate rabbitTemplateSystem() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(qSystem);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
    @Bean(name="rabbitTemplateArch")
    public RabbitTemplate rabbitTemplateArch() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(qArch);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
