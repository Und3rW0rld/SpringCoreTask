package com.uw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class for producing and sending messages to a JMS destination.
 */
@Service
public class MessageProducer {

      private final JmsTemplate jmsTemplate;

      /**
       * Constructor for MessageProducer.
       *
       * @param jmsTemplate the JmsTemplate to be used for sending messages
       */
      @Autowired
      public MessageProducer(JmsTemplate jmsTemplate) {
            this.jmsTemplate = jmsTemplate;
      }

      /**
       * Sends a message to the specified JMS destination.
       *
       * @param destination the destination to which the message will be sent
       * @param message the message to be sent
       */
      public void sendMessage(String destination, String message) {
            jmsTemplate.convertAndSend(destination, message);
      }
}