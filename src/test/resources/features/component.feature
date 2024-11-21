Feature: Component tests for TrainingController

  Scenario: Create a new training session
    Given a valid training request
    When the request is sent to the create endpoint
    Then the training session is created successfully

  Scenario: Delete an existing training session
    Given an existing training session ID
    When the request is sent to the delete endpoint
    Then the training session is deleted successfully

  Scenario: Create a training session with invalid data
    Given an invalid training request with missing or incorrect fields
    When the request is sent to the create endpoint
    Then the system returns a 400 Bad Request error
    And an error message is displayed indicating which fields are invalid

  Scenario: Create a training session for a non-existent trainer
    Given a training request with a trainer ID that does not exist
    When the request is sent to the create endpoint
    Then the system returns a 404 Not Found error
    And an error message is displayed indicating that the trainer was not found

  Scenario: Delete a non-existent training session
    Given a training session ID that does not exist
    When the request is sent to the delete endpoint
    Then the system returns a 404 Not Found error
    And an error message is displayed indicating that the session was not found
