package com.uw.steps;

import com.uw.dto.TrainingRequestDTO;
import com.uw.model.Trainee;
import com.uw.model.Trainer;
import com.uw.model.Training;
import com.uw.model.User;
import com.uw.repository.TraineeRepository;
import com.uw.repository.TrainerRepository;
import com.uw.repository.TrainingRepository;
import com.uw.service.TrainingManagementService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class ComponentTestSteps {

      @Autowired
      private TrainingManagementService trainingManagementService;

      @Autowired
      private TrainingRepository repository;

      @Autowired
      private TrainerRepository trainerRepository;

      @Autowired
      private TraineeRepository traineeRepository;

      private TrainingRequestDTO trainingRequest;
      private ResponseEntity<?> response;

      @Given("a valid training request")
      public void aValidTrainingRequest() {
            // Clear repository before each test
            repository.deleteAll();

            // Create mock data for trainees and trainers
            Trainee trainee = new Trainee();
            User user = new User();
            user.setUsername("john_doe");
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setActive(true);
            user.setPassword("password");
            trainee.setUser(user);
            traineeRepository.save(trainee);

            Trainer trainer = new Trainer();
            user = new User();
            user.setUsername("jane_smith");
            user.setFirstName("Jane");
            user.setLastName("Smith");
            user.setPassword("password");
            user.setActive(true);
            trainer.setUser(user);
            trainerRepository.save(trainer);

            // Create a new training request DTO
            trainingRequest = new TrainingRequestDTO();
            trainingRequest.setTrainingName("Advanced Java");
            trainingRequest.setTrainingDate(LocalDate.of(2024, 1, 15));
            trainingRequest.setTraineeUsername("john_doe");
            trainingRequest.setTrainerUsername("jane_smith");
      }


      @When("the request is sent to the create endpoint")
      public void theRequestIsSentToTheCreateEndpoint() {
            response = trainingManagementService.createTraining(trainingRequest).join();
      }

      @Then("the training session is created successfully")
      public void theTrainingSessionIsCreatedSuccessfully() {
            assertEquals(200, response.getStatusCode().value());
      }

      @Then("the training session exists in the database")
      public void theTrainingSessionExistsInTheDatabase() {

            Training training = repository.findByTrainingName( trainingRequest.getTrainingName() );
            assertNotNull(training);
      }

      @Given("an existing training session ID")
      public void anExistingTrainingSessionID() {
            // Set existing training session ID
      }

      @When("the request is sent to the delete endpoint")
      public void theRequestIsSentToTheDeleteEndpoint() {
            response = trainingManagementService.deleteTraining(1L).join();
      }

      @Then("the training session is deleted successfully")
      public void theTrainingSessionIsDeletedSuccessfully() {
            assertEquals(200, response.getStatusCode().value());
      }

      @Then("the training session does not exist in the database")
      public void theTrainingSessionDoesNotExistInTheDatabase() {
            Training training = repository.findByTrainingName( trainingRequest.getTrainingName() );
            assertNull(training);
      }

      @Given("an invalid training request with missing or incorrect fields")
      public void anInvalidTrainingRequestWithMissingOrIncorrectFields() {
            trainingRequest = new TrainingRequestDTO();
            trainingRequest.setTrainingName(null); // Missing training name
            trainingRequest.setTrainingDate(LocalDate.of(2024, 1, 15));
            trainingRequest.setTraineeUsername(null); // Missing trainee username
            trainingRequest.setTrainerUsername("jane_smith");
      }

      @Then("the system returns a 400 Bad Request error")
      public void theSystemReturnsABadRequestError() {
            assertEquals(400, response.getStatusCode().value());
      }

      @Then("an error message is displayed indicating which fields are invalid")
      public void anErrorMessageIsDisplayedIndicatingInvalidFields() {
            assertTrue(response.getBody().toString().contains("Invalid training request"));
      }

      @Given("a training request with a trainer ID that does not exist")
      public void aTrainingRequestWithNonExistentTrainerID() {
            trainingRequest = new TrainingRequestDTO();
            trainingRequest.setTrainingName("Advanced Java");
            trainingRequest.setTrainingDate(LocalDate.of(2024, 1, 15));
            trainingRequest.setTraineeUsername("john_doe");
            trainingRequest.setTrainerUsername("non_existent_trainer");
      }

      @Then("the system returns a 404 Not Found error")
      public void theSystemReturnsANotFoundError() {
            assertEquals(404, response.getStatusCode().value());
      }

      @Then("an error message is displayed indicating that the trainer was not found")
      public void anErrorMessageIsDisplayedForNonExistentTrainer() {
            assertTrue(response.getBody().toString().contains("not found"));
      }

      @Given("a training session ID that does not exist")
      public void aNonExistentTrainingSessionID() {
            // Assuming ID 9999 does not exist
            trainingRequest = new TrainingRequestDTO();
      }

      @When("the request is sent to the delete endpoint for a non-existent training session")
      public void theRequestIsSentToTheDeleteEndpointForNonExistentSession() {
            response = trainingManagementService.deleteTraining(9999L).join();
      }

      @Then("an error message is displayed indicating that the session was not found")
      public void anErrorMessageIsDisplayedForNonExistentSession() {
            assertTrue(response.getBody().toString().contains("Training not found"));
      }

}