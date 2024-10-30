package com.uw.controller;

import com.uw.dto.TrainingRequestDTO;
import com.uw.dto.TrainingTypeDTO;
import com.uw.model.TrainingType;
import com.uw.service.TrainingManagementService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
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
       * @param token the authorization token
       * @return a CompletableFuture containing the ResponseEntity
       */
      @PostMapping
      @CircuitBreaker(name = "trainerWorkloadService", fallbackMethod = "fallbackCreateTraining")
      @TimeLimiter(name = "trainerWorkloadService")
      public CompletableFuture<ResponseEntity<?>> create(@RequestBody TrainingRequestDTO trainingRequest,
                                                         @RequestHeader("Authorization") String token) {
            return trainingManagementService.createTraining(trainingRequest, token);
      }

      /**
       * Deletes an existing training session.
       *
       * @param id the ID of the training session to delete
       * @param token the authorization token
       * @return a CompletableFuture containing the ResponseEntity
       */
      @DeleteMapping("/{id}")
      @CircuitBreaker(name = "trainerWorkloadService", fallbackMethod = "fallbackDeleteTraining")
      @TimeLimiter(name = "trainerWorkloadService")
      public CompletableFuture<ResponseEntity<?>> delete(@PathVariable Long id,
                                                         @RequestHeader("Authorization") String token) {
            return trainingManagementService.deleteTraining(id, token);
      }

      /**
       * Retrieves all unique training types.
       *
       * @return a ResponseEntity containing the list of training type DTOs
       */
      @GetMapping("/types")
      public ResponseEntity<?> getTrainingTypes() {
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
       * @param token the authorization token
       * @param throwable the exception that caused the fallback
       * @return a CompletableFuture containing the ResponseEntity with an error message
       */
      public CompletableFuture<ResponseEntity<String>> fallbackCreateTraining(TrainingRequestDTO trainingRequest, String token, Throwable throwable) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is currently unavailable. Please try again later.")
            );
      }

      /**
       * Fallback method for delete training when the circuit breaker is triggered.
       *
       * @param id the ID of the training session to delete
       * @param token the authorization token
       * @param throwable the exception that caused the fallback
       * @return a CompletableFuture containing the ResponseEntity with an error message
       */
      public CompletableFuture<ResponseEntity<String>> fallbackDeleteTraining(Long id, String token, Throwable throwable) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Unable to delete training at this time. Please try again later.")
            );
      }
}