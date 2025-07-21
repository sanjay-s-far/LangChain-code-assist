import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.jms.ChannelPublishingJmsMessageListener;
import org.springframework.integration.jms.JmsMessageDrivenEndpoint;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import com.ibm.mq.spring.boot.MQConfigurationProperties;
import com.ibm.mq.spring.boot.MQConnectionFactoryCustomizer;
import com.ibm.mq.spring.boot.MQConnectionFactoryFactory;

import jakarta.jms.ConnectionFactory;

@Configuration
@EnableIntegration
public class InboundGatewayConfiguration {

	@Value("${mq.concurrent.listener.count}")
	private String concurrentListeners;
	
	@Value("${app.mq.tasks.queue.capacity}")
	private int capacity;
	@Value("${app.mq.tasks.queue.max.pool.size}")
	private int poolSize;
	@Value("${app.mq.tasks.queue.core.pool.size}")
	private int corePoolSize;
	
	
	

	@Value("${source.queue.name}")
	private String sourceQueue;

	
	@Value("${spring.application.name}")
	private String appName;

	
	@Bean
	@ConfigurationProperties("ibm.mq")
	public MQConfigurationProperties qm1ConfigProperties() {
		return new MQConfigurationProperties();
	}

	@Bean
	public MQConnectionFactory qm1ConnectionFactory(
			@Qualifier("qm1ConfigProperties") MQConfigurationProperties properties,
			ObjectProvider<List<MQConnectionFactoryCustomizer>> factoryCustomizers) {
		return new MQConnectionFactoryFactory(properties, factoryCustomizers.getIfAvailable())
				.createConnectionFactory(MQConnectionFactory.class);
	}

	@Bean
	@ConfigurationProperties("ibm.mq02")
	public MQConfigurationProperties qm2ConfigProperties() {
		return new MQConfigurationProperties();
	}

	@Bean
	public MQConnectionFactory qm2ConnectionFactory(
			@Qualifier("qm2ConfigProperties") MQConfigurationProperties properties,
			ObjectProvider<List<MQConnectionFactoryCustomizer>> factoryCustomizers) {
		return new MQConnectionFactoryFactory(properties, factoryCustomizers.getIfAvailable())
				.createConnectionFactory(MQConnectionFactory.class);
	}

	@Bean("bqJmsTemplate")
	public JmsTemplate jmsTemplate(@Qualifier("qm2ConnectionFactory") ConnectionFactory connectionFactory) {
		return new JmsTemplate(connectionFactory);
	}
	@Bean
	public DirectChannel consumingChannel1() {
		return new DirectChannel();
	}

	@Bean
	public ChannelPublishingJmsMessageListener channelPublishingJmsMessageListener1() {
		return new ChannelPublishingJmsMessageListener();
	}

	@Bean
	public JmsMessageDrivenEndpoint jmsMessageDrivenEndpoint(

			@Qualifier("qm1ConnectionFactory") ConnectionFactory connectionFactory) throws Exception {
		JmsMessageDrivenEndpoint endpoint = new JmsMessageDrivenEndpoint(
				defaultMessageListenerContainer(connectionFactory), channelPublishingJmsMessageListener1());
		endpoint.setOutputChannel(consumingChannel1());
		endpoint.setErrorChannel(errorChannel());
		return endpoint;
	}

	@Bean

	@ServiceActivator(inputChannel = "consumingChannel1")
	public CouponMaintenanceService countDownLatchHandler() {
		return new CouponMaintenanceService();
	}

	@Bean
	public DirectChannel consumingChannel2() {
		return new DirectChannel();
	}

	@Bean
	public ChannelPublishingJmsMessageListener channelPublishingJmsMessageListener2() {
		return new ChannelPublishingJmsMessageListener();
	}

	@Bean
	public JmsMessageDrivenEndpoint jmsMessageDrivenEndpoint2(

			@Qualifier("qm2ConnectionFactory") ConnectionFactory connectionFactory) throws Exception {
		JmsMessageDrivenEndpoint endpoint = new JmsMessageDrivenEndpoint(
				defaultMessageListenerContainer2(connectionFactory), channelPublishingJmsMessageListener2());
		endpoint.setOutputChannel(consumingChannel2());
		endpoint.setErrorChannel(errorChannel());
		return endpoint;
	}

	@Bean

	@ServiceActivator(inputChannel = "consumingChannel2")
	public CouponMaintenanceService processMessage() {
		return new CouponMaintenanceService();
	}

	@Bean
	public DirectChannel errorChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "errorChannel")
	public ErrorHandler errorHandler() {
		return new ErrorHandler();
	}

	@Bean
	DefaultMessageListenerContainer defaultMessageListenerContainer(
			@Qualifier("qm1ConnectionFactory") ConnectionFactory connectionFactory) throws Exception {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		// container.setMessageListener(listnerService1());
		container.setConnectionFactory(connectionFactory);
		container.setDestinationName(sourceQueue);
		container.setTaskExecutor(taskExecutor());
		container.setConcurrency(concurrentListeners);
		container.setSessionAcknowledgeMode(2);
		container.setSessionTransacted(true);

		return container;
	}

	@Bean
	DefaultMessageListenerContainer defaultMessageListenerContainer2(
			@Qualifier("qm2ConnectionFactory") ConnectionFactory connectionFactory) throws Exception {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();

		container.setConnectionFactory(connectionFactory);
		container.setDestinationName(sourceQueue);
		container.setTaskExecutor(taskExecutor());
		container.setConcurrency(concurrentListeners);
		container.setSessionAcknowledgeMode(2);
		container.setSessionTransacted(true);

		return container;
	}

	@Bean
	TaskExecutor taskExecutor() throws Exception {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(corePoolSize);
		taskExecutor.setMaxPoolSize(poolSize);
		taskExecutor.setQueueCapacity(capacity);
		taskExecutor.setThreadGroupName(appName);

		return taskExecutor;
	}

}
