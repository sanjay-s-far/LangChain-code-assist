package com.aeo.coupon.maintenance.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.aeo.coupon.maintenance.service.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
public class ErrorHandler {

	@Autowired
	@Qualifier("bqJmsTemplate")
	private JmsTemplate jmsTemplate;

	@Value("${backout.bq.name}")
	private String backoutQueue;

	public void handleMessage(Message<String> message) {
		try {
			jmsTemplate.convertAndSend(backoutQueue, message, msg -> {
				msg.setJMSTimestamp(System.nanoTime());

				return msg;
			});
			log.info("INFO:ErrorHandler : Message sent to BackoutQueue:{}:{}",
					backoutQueue,message.toString().replaceAll("\n\t*|\t", " "));
		} catch (Exception ex) {
			log.error("ERROR:ErrorHandler :Error while sending message to BackoutQueue - {}: {}:{}",
					backoutQueue,message.toString().replaceAll("\n\t*|\t", " "), ExceptionUtils.getErrorMessage(ex));
		}

	}
	
	

}
