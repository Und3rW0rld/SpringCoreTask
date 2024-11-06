package com.uw.controller;

import com.uw.dto.TrainingRequestDTO;
import com.uw.dto.TrainingTypeDTO;
import com.uw.model.TrainingType;
import com.uw.service.TrainingManagementService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * REST controller for managing training sessions.
 */
@RestController
@RequestMapping("api/v1/trainings")
public class TrainingController {

      private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);

      private final TrainingManagementService trainingManagementService;

      /**
       * Constructor for TrainingController.
       *
       * @param trainingManagementService the service to manage training operations
       */
      @Autowired
      public TrainingController(TrainingManagementService trainingManagementService) {
            this.trainingManagementService = trainingManagementService;
      }

      /**
       * Creates a new training session.
       *
       * @param trainingRequest the training request data transfer object
       * @return a CompletableFuture containing the ResponseEntity
       */
      @PostMapping
      @CircuitBreaker(name = "trainerWorkloadService", fallbackMethod = "fallbackCreateTraining")
      @TimeLimiter(name = "trainerWorkloadService")
      public CompletableFuture<ResponseEntity<?>> create(@RequestBody TrainingRequestDTO trainingRequest) {
            logger.info("Creating new training session with request: {}", trainingRequest);
            return trainingManagementService.createTraining(trainingRequest);
      }

      /**
       * Deletes an existing training session.
       *
       * @param id the ID of the training session to delete
       * @return a CompletableFuture containing the ResponseEntity
       */
      @DeleteMapping("/{id}")
      @CircuitBreaker(name = "trainerWorkloadService", fallbackMethod = "fallbackDeleteTraining")
      @TimeLimiter(name = "trainerWorkloadService")
      public CompletableFuture<ResponseEntity<?>> delete(@PathVariable Long id) {
            logger.info("Deleting training session with ID: {}", id);
            return trainingManagementService.deleteTraining(id);
      }

      /**
       * Retrieves all unique training types.
       *
       * @return a ResponseEntity containing the list of training type DTOs
       */
      @GetMapping("/types")
      public ResponseEntity<?> getTrainingTypes() {
            logger.info("Retrieving all unique training types");
            Set<TrainingType> uniqueTrainingTypes = trainingManagementService.getUniqueTrainingTypes();
            List<TrainingTypeDTO> trainingTypeDTOs = uniqueTrainingTypes.stream()
                    .map(trainingType -> new TrainingTypeDTO(trainingType.getId(), trainingType.getTypeName().name()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(trainingTypeDTOs);
      }

      /**
       * Fallback method for create training when the circuit breaker is triggered.
       *
       * @param trainingRequest the training request data transfer object
       * @param throwable the exception that caused the fallback
       * @return a CompletableFuture containing the ResponseEntity with an error message
       */
      public CompletableFuture<ResponseEntity<String>> fallbackCreateTraining(TrainingRequestDTO trainingRequest, Throwable throwable) {
            logger.error("Fallback for create training triggered due to: {}", throwable.getMessage());
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is currently unavailable. Please try again later.")
            );
      }

      /**
       * Fallback method for delete training when the circuit breaker is triggered.
       *
       * @param id the ID of the training session to delete
       * @param throwable the exception that caused the fallback
       * @return a CompletableFuture containing the ResponseEntity with an error message
       */
      public CompletableFuture<ResponseEntity<String>> fallbackDeleteTraining(Long id, Throwable throwable) {
            logger.error("Fallback for delete training triggered due to: {}", throwable.getMessage());
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Unable to delete training at this time. Please try again later.")
            );
      }
}