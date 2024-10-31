package com.uw.service;

import com.uw.client.TrainerWorkloadClient;
import com.uw.dto.TrainingRequestDTO;
import com.uw.model.Trainee;
import com.uw.model.Trainer;
import com.uw.model.Training;
import com.uw.model.TrainingType;
import com.uw.model.User;
import com.uw.util.ErrorMessages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TrainingManagementService class.
 */
@ExtendWith(MockitoExtension.class)
class TrainingManagementServiceTest {

      @Mock
      private TraineeService traineeService;

      @Mock
      private TrainerService trainerService;

      @Mock
      private TrainingService trainingService;

      @Mock
      private TrainerWorkloadClient trainerWorkloadClient;

      @InjectMocks
      private TrainingManagementService trainingManagementService;


      /**
       * Tests the scenario where the trainee is not found.
       */
      @Test
      void createTraining_traineeNotFound() {
            TrainingRequestDTO trainingRequestDTO = new TrainingRequestDTO();
            trainingRequestDTO.setTraineeUsername("trainee1");

            when(traineeService.findTraineeByUsername("trainee1")).thenReturn(null);

            CompletableFuture<ResponseEntity<?>> response = trainingManagementService.createTraining(trainingRequestDTO);

            assertEquals(HttpStatus.NOT_FOUND, response.join().getStatusCode());
            assertEquals(ErrorMessages.TRAINEE_NOT_FOUND, response.join().getBody());
      }

      /**
       * Tests the scenario where the trainer is not found.
       */
      @Test
      void createTraining_trainerNotFound() {
            TrainingRequestDTO trainingRequestDTO = new TrainingRequestDTO();
            trainingRequestDTO.setTraineeUsername("trainee1");
            trainingRequestDTO.setTrainerUsername("trainer1");

            Trainee trainee = new Trainee();

            when(traineeService.findTraineeByUsername("trainee1")).thenReturn(trainee);
            when(trainerService.findTrainerByUsername("trainer1")).thenReturn(null);

            CompletableFuture<ResponseEntity<?>> response = trainingManagementService.createTraining(trainingRequestDTO);

            assertEquals(HttpStatus.NOT_FOUND, response.join().getStatusCode());
            assertEquals(ErrorMessages.TRAINER_NOT_FOUND, response.join().getBody());
      }

      /**
       * Tests the scenario where the training session to be deleted is not found.
       */
      @Test
      void deleteTraining_notFound() {
            when(trainingService.findById(1L)).thenReturn(null);

            CompletableFuture<ResponseEntity<?>> response = trainingManagementService.deleteTraining(1L);

            assertEquals(HttpStatus.NOT_FOUND, response.join().getStatusCode());
            assertEquals(ErrorMessages.TRAINING_NOT_FOUND, response.join().getBody());
      }

      /**
       * Tests the retrieval of unique training types.
       */
      @Test
      void getOtherTrainingTypes_success() {
            TrainingType trainingType = new TrainingType();
            trainingType.setTypeName("Java Basics");

            Training training = new Training();
            training.setTrainingType(trainingType);

            when(trainingService.findAll()).thenReturn(List.of(training));

            Set<TrainingType> uniqueTrainingTypes = trainingManagementService.getUniqueTrainingTypes();

            assertEquals(1, uniqueTrainingTypes.size());
            assertEquals("OTHER", uniqueTrainingTypes.iterator().next().getTypeName().name());
      }
}