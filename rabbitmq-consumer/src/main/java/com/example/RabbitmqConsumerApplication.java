package com.example;

import com.example.listeners.impl.ListenerGroup;
import com.example.listeners.impl.ListenerStudent;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class RabbitmqConsumerApplication {
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


	private static final String LISTENER_METHOD = "receiveMessage";

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqConsumerApplication.class, args);
	}

	@Bean(name ="queueGroup")
	Queue queueGroup() {
		return new Queue(qGroup, true);
	}

	@Bean(name="exchangeGroup")
	TopicExchange exchangeGroup() {
		return new TopicExchange(topicName);
	}

	@Bean(name="bindingGroup")
	Binding bindingCustomer(Queue queueGroup, TopicExchange exchangeGroup) {
		return BindingBuilder.bind(queueGroup).to(exchangeGroup).with(qGroup);
	}

	@Bean(name="queueStudent")
	Queue queueStudent() {
		return new Queue(qStudent, true);
	}

	@Bean(name="exchangeStudent")
	TopicExchange exchangeStudent() {
		return new TopicExchange(topicName);
	}

	@Bean(name="bindingStudent")
	Binding bindingShop(Queue queueStudent, TopicExchange exchangeStudent) {
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

	@Bean(name="containerGroup")
	SimpleMessageListenerContainer containerGroup(ConnectionFactory connectionFactory,
													 MessageListenerAdapter listenerAdapterGroup) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageConverter(jsonMessageConverter());
		container.setQueueNames(qGroup);
		container.setMessageListener(listenerAdapterGroup);
		return container;
	}

	@Bean(name="listenerAdapterGroup")
	public MessageListenerAdapter listenerAdapterGroup(ListenerGroup receiver) {
		MessageListenerAdapter msgAdapter = new MessageListenerAdapter(receiver);
		msgAdapter.setMessageConverter(jsonMessageConverter());
		msgAdapter.setDefaultListenerMethod(LISTENER_METHOD);

		return msgAdapter;
	}

	@Bean(name="containerStudent")
	SimpleMessageListenerContainer containerStudent(ConnectionFactory connectionFactory,
													 MessageListenerAdapter listenerAdapterStudent) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageConverter(jsonMessageConverter());
		container.setQueueNames(qStudent);
		container.setMessageListener(listenerAdapterStudent);
		return container;
	}

	@Bean(name="listenerAdapterStudent")
	public MessageListenerAdapter listenerAdapterStudent(ListenerStudent receiver) {
		MessageListenerAdapter msgAdapter = new MessageListenerAdapter(receiver);
		msgAdapter.setMessageConverter(jsonMessageConverter());
		msgAdapter.setDefaultListenerMethod(LISTENER_METHOD);

		return msgAdapter;
	}
}
