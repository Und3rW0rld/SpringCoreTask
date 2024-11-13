Feature: Component tests for TrainingController

  Scenario: Create a new training session
    Given a valid training request
    When the request is sent to the create endpoint
    Then the training session is created successfully

  Scenario: Delete an existing training session
    Given an existing training session ID
    When the request is sent to the delete endpoint
    Then the training session is deleted successfully