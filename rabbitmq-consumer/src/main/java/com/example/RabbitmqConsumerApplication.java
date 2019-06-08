package com.example;

import com.example.listeners.impl.ListenerStudentGroupTeacher;
import com.example.listeners.impl.ListenerSystem;
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
	@Value("${queue.studentGroupTeacher.name}")
	private String qStudentGroupTeacher;

	@Value("${queue.arch.name}")
	private String qArch;

	@Value("${queue.lesSub.name}")
	private String qLesSub;

	@Value("${queue.system.name}")
	private String qSystem;

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

	@Bean(name ="queueStudentGroupTeacher")
	Queue queueStudentGroupTeacher() {
		return new Queue(qStudentGroupTeacher, true);
	}
	@Bean(name="queueArch")
	Queue queueArch() {
		return new Queue(qArch, true);
	}
	@Bean(name ="queueSystem")
	Queue queueSystem() {
		return new Queue(qSystem, true);
	}
	@Bean(name="queueLesSub")
	Queue queueLesSub() {
		return new Queue(qLesSub, true);
	}


	@Bean(name="exchangeStudentGroupTeacher")
	TopicExchange exchangeStudentGroupTeacher() {
		return new TopicExchange(topicName);
	}
	@Bean(name="exchangeSystem")
	TopicExchange exchangeSystem() {
		return new TopicExchange(topicName);
	}
	@Bean(name="exchangeLesSub")
	TopicExchange exchangeLesSub() {
		return new TopicExchange(topicName);
	}
	@Bean(name="exchangeArch")
	TopicExchange exchangeArch() {
		return new TopicExchange(topicName);
	}


	@Bean(name="bindingStudentGroupTeacher")
	Binding bindingStudentGroupTeacher(Queue queueStudentGroupTeacher, TopicExchange exchangeStudentGroupTeacher) {
		return BindingBuilder.bind(queueStudentGroupTeacher).to(exchangeStudentGroupTeacher).with(qStudentGroupTeacher);
	}
	@Bean(name="bindingSystem")
	Binding bindingSystem(Queue queueSystem, TopicExchange exchangeSystem) {
		return BindingBuilder.bind(queueSystem).to(exchangeSystem).with(qSystem);
	}
	@Bean(name="bindingArch")
	Binding bindingArch(Queue queueArch, TopicExchange exchangeArch) {
		return BindingBuilder.bind(queueArch).to(exchangeArch).with(qArch);
	}
	@Bean(name="bindingLesSub")
	Binding bindingLesSub(Queue queueLesSub, TopicExchange exchangeLesSub) {
		return BindingBuilder.bind(queueLesSub).to(exchangeLesSub).with(qLesSub);
	}


	@Bean(name="containerStudentGroupTeacher")
	SimpleMessageListenerContainer containerStudentGroupTeacher(ConnectionFactory connectionFactory,
													 MessageListenerAdapter listenerAdapterStudentGroupTeacher) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageConverter(jsonMessageConverter());
		container.setQueueNames(qStudentGroupTeacher);
		container.setMessageListener(listenerAdapterStudentGroupTeacher);
		return container;
	}
	@Bean(name="containerSystem")
	SimpleMessageListenerContainer containerSystem(ConnectionFactory connectionFactory,
													MessageListenerAdapter listenerAdapterSystem) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageConverter(jsonMessageConverter());
		container.setQueueNames(qSystem);
		container.setMessageListener(listenerAdapterSystem);
		return container;
	}
	@Bean(name="containerArch")
	SimpleMessageListenerContainer containerArch(ConnectionFactory connectionFactory,
												  MessageListenerAdapter listenerAdapterArch) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageConverter(jsonMessageConverter());
		container.setQueueNames(qArch);
		container.setMessageListener(listenerAdapterArch);
		return container;
	}
	@Bean(name="containerLesSub")
	SimpleMessageListenerContainer containerLesSub(ConnectionFactory connectionFactory,
													MessageListenerAdapter listenerAdapterLesSub) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageConverter(jsonMessageConverter());
		container.setQueueNames(qLesSub);
		container.setMessageListener(listenerAdapterLesSub);
		return container;
	}


	@Bean(name="listenerAdapterStudentGroupTeacher")
	public MessageListenerAdapter listenerAdapterStudentGroupTeacher(ListenerStudentGroupTeacher receiver) {
		MessageListenerAdapter msgAdapter = new MessageListenerAdapter(receiver);
		msgAdapter.setMessageConverter(jsonMessageConverter());
		msgAdapter.setDefaultListenerMethod(LISTENER_METHOD);

		return msgAdapter;
	}
	@Bean(name="listenerAdapterSystem")
	public MessageListenerAdapter listenerAdapterSystem(ListenerSystem receiver) {
		MessageListenerAdapter msgAdapter = new MessageListenerAdapter(receiver);
		msgAdapter.setMessageConverter(jsonMessageConverter());
		msgAdapter.setDefaultListenerMethod(LISTENER_METHOD);

		return msgAdapter;
	}
	@Bean(name="listenerAdapterArch")
	public MessageListenerAdapter listenerAdapterArch(ListenerStudentGroupTeacher receiver) {
		MessageListenerAdapter msgAdapter = new MessageListenerAdapter(receiver);
		msgAdapter.setMessageConverter(jsonMessageConverter());
		msgAdapter.setDefaultListenerMethod(LISTENER_METHOD);

		return msgAdapter;
	}
	@Bean(name="listenerAdapterLesSub")
	public MessageListenerAdapter listenerAdapterLesSub(ListenerSystem receiver) {
		MessageListenerAdapter msgAdapter = new MessageListenerAdapter(receiver);
		msgAdapter.setMessageConverter(jsonMessageConverter());
		msgAdapter.setDefaultListenerMethod(LISTENER_METHOD);

		return msgAdapter;
	}


}
