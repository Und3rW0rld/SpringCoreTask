package com.uw.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jms.core.JmsTemplate;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the MessageProducer service.
 */
public class MessageProducerServiceTest {

      @Mock
      private JmsTemplate jmsTemplate;

      @InjectMocks
      private MessageProducer messageProducerService;

      /**
       * Initializes mocks for the test class.
       */
      public MessageProducerServiceTest() {
            MockitoAnnotations.openMocks(this);
      }

      /**
       * Tests the sendMessage method to ensure it sends a message to the specified JMS destination.
       */
      @Test
      public void testSendMessage() {
            // Arrange
            String queueName = "training.queue";
            String message = "Test Message";

            // Act
            messageProducerService.sendMessage(queueName, message);

            // Assert
            verify(jmsTemplate, times(1)).convertAndSend(queueName, message);
      }
}