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
}