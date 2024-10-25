package com.uw.controller;

import com.uw.client.TrainerWorkloadClient;
import com.uw.dto.TrainingRequest;
import com.uw.dto.TrainingRequestDTO;
import com.uw.dto.TrainingTypeDTO;
import com.uw.model.Trainee;
import com.uw.model.Trainer;
import com.uw.model.Training;
import com.uw.model.TrainingType;
import com.uw.service.TraineeService;
import com.uw.service.TrainerService;
import com.uw.service.TrainingService;
import com.uw.util.ErrorMessages;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/trainings")
public class TrainingController {

      private final TraineeService traineeService;
      private final TrainerService trainerService;
      private final TrainingService trainingService;
      private final TrainerWorkloadClient trainerWorkloadClient;

      @Autowired
      public TrainingController(TraineeService traineeService,
                                TrainerService trainerService, TrainingService trainingService,
                                TrainerWorkloadClient trainerWorkloadClient) {
            this.traineeService = traineeService;
            this.trainerService = trainerService;
            this.trainingService = trainingService;
            this.trainerWorkloadClient = trainerWorkloadClient;
      }

      @PostMapping
      @CircuitBreaker(name = "trainerWorkloadService", fallbackMethod = "fallbackCreateTraining")
      @TimeLimiter(name = "trainerWorkloadService")
      public CompletableFuture<ResponseEntity<?>> create(
              @RequestBody TrainingRequestDTO trainingRequest,
              @RequestHeader("Authorization") String token
      ){
            Trainee trainee = traineeService.findTraineeByUsername(trainingRequest.getTraineeUsername());
            if (trainee == null) {
                  return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.TRAINEE_NOT_FOUND));
            }

            Trainer trainer = trainerService.findTrainerByUsername(trainingRequest.getTrainerUsername());
            if (trainer == null) {
                  return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.TRAINER_NOT_FOUND));
            }

            Training training = new Training();
            training.setTrainee(trainee);
            training.setTrainer(trainer);
            training.setTrainingName(trainingRequest.getTrainingName());
            training.setTrainingDate(trainingRequest.getTrainingDate());
            training.setTrainingDuration(trainingRequest.getTrainingDuration());
            TrainingType trainingType = new TrainingType();
            trainingType.setTypeName(trainingRequest.getTrainingName());
            training.setTrainingType(trainingType);

            TrainingRequest request = getTrainingRequest(trainer, trainingRequest.getTrainingDate(), trainingRequest.getTrainingDuration(), "add");

            trainerWorkloadClient.trainingRequest(request, token);

            trainingService.createTraining(training);

            return CompletableFuture.completedFuture(ResponseEntity.ok().build());
      }

      @DeleteMapping("/{id}")
      @CircuitBreaker(name = "trainerWorkloadService", fallbackMethod = "fallbackDeleteTraining")
      @TimeLimiter(name = "trainerWorkloadService")
      public CompletableFuture<ResponseEntity<?>> delete(@PathVariable Long id,
                                                         @RequestHeader("Authorization") String token) {
            Training training = trainingService.findById(id);
            if (training == null) {
                  return CompletableFuture.completedFuture( ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.TRAINING_NOT_FOUND) );
            }

            TrainingRequest request = getTrainingRequest(training.getTrainer(), training.getTrainingDate(), training.getTrainingDuration(), "delete");

            trainerWorkloadClient.trainingRequest(request, token);

            trainingService.deleteTraining(id);

            return CompletableFuture.completedFuture( ResponseEntity.ok().build() );
      }

      private static TrainingRequest getTrainingRequest(Trainer trainer, LocalDate trainingDate, int trainingDuration, String action) {
            TrainingRequest request = new TrainingRequest();
            request.setTrainerUsername(trainer.getUser().getUsername());
            request.setFirstName(trainer.getUser().getFirstName());
            request.setLastName(trainer.getUser().getLastName());
            request.setActive(trainer.getUser().isActive());
            request.setTrainingDate(trainingDate);
            request.setTrainingDuration(trainingDuration);
            request.setActionType(action);
            return request;
      }

      @GetMapping("/types")
      public ResponseEntity<?> getTrainingTypes() {
            List<Training> trainings = trainingService.findAll();
            Set<TrainingType> uniqueTrainingTypes = new HashSet<>();

            for (Training training : trainings) {
                  uniqueTrainingTypes.add(training.getTrainingType());
            }

            List<TrainingTypeDTO> trainingTypeDTOs = uniqueTrainingTypes.stream()
                    .map(trainingType -> new TrainingTypeDTO(trainingType.getId(), trainingType.getTypeName().name()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(trainingTypeDTOs);
      }

      // Métodos de fallback para los casos en los que el servicio no esté disponible
      public CompletableFuture<ResponseEntity<String>> fallbackCreateTraining(TrainingRequestDTO trainingRequest, String token, Throwable throwable) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is currently unavailable. Please try again later.")
            );
      }

      public ResponseEntity<String> fallbackDeleteTraining(Long id, String token, Throwable throwable) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Unable to delete training at this time. Please try again later.");
      }
}
