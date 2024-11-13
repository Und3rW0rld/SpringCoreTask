Feature: Message Producer Integration Test

  Scenario: Verify that a message is sent to and received from the queue
    Given a message with content "Test Message" and queue "training.queue"
    When the message is sent by the producer
    Then the message is received by the queue consumer
