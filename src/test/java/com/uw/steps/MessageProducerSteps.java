package com.uw.steps;

import com.uw.service.MessageProducer;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MessageProducerSteps {

      @Autowired
      private JmsTemplate jmsTemplate;

      @Autowired
      private MessageProducer messageProducer;

      private String queueName;
      private String messageContent;
      private Message receivedMessage;

      @Given("a message with content {string} and queue {string}")
      public void aMessageWithContentAndQueue(String content, String queue) {
            this.messageContent = content;
            this.queueName = queue;
      }

      @When("the message is sent by the producer")
      public void theMessageIsSentByTheProducer() {
            messageProducer.sendMessage(queueName, messageContent);
      }

      @Then("the message is received by the queue consumer")
      public void theMessageIsReceivedByTheQueueConsumer() throws JMSException {
            receivedMessage = jmsTemplate.receive(queueName);
            assertNotNull(receivedMessage, "Expected a message to be received");

            if (receivedMessage instanceof TextMessage textMessage) {
                  String payload = textMessage.getText();

                  // Parse the JSON payload to compare the specific field
                  assertTrue(payload.contains("\"trainerUsername\":\"jane_smith\""), "Expected payload to contain trainerUsername field");
                  assertTrue(payload.contains("\"firstName\":\"Jane\""), "Expected payload to contain firstName field");

                  // Add more field checks as needed
            } else {
                  fail("Expected a TextMessage but received: " + receivedMessage.getClass().getSimpleName());
            }
      }

}
