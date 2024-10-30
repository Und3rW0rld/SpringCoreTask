package com.uw.service;

import com.uw.client.TrainerWorkloadClient;
import com.uw.dto.TrainingRequest;
import com.uw.dto.TrainingRequestDTO;
import com.uw.model.Trainee;
import com.uw.model.Trainer;
import com.uw.model.Training;
import com.uw.model.TrainingType;
import com.uw.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Service class for managing training operations.
 */
@Service
public class TrainingManagementService {

      private final TraineeService traineeService;
      private final TrainerService trainerService;
      private final TrainingService trainingService;
      private final TrainerWorkloadClient trainerWorkloadClient;

      /**
       * Constructor for TrainingManagementService.
       *
       * @param traineeService the service to manage trainees
       * @param trainerService the service to manage trainers
       * @param trainingService the service to manage trainings
       * @param trainerWorkloadClient the client to manage trainer workload
       */
      @Autowired
      public TrainingManagementService(TraineeService traineeService, TrainerService trainerService,
                                       TrainingService trainingService, TrainerWorkloadClient trainerWorkloadClient) {
            this.traineeService = traineeService;
            this.trainerService = trainerService;
            this.trainingService = trainingService;
            this.trainerWorkloadClient = trainerWorkloadClient;
      }

      /**
       * Creates a new training session.
       *
       * @param trainingRequest the training request data transfer object
       * @param token the authorization token
       * @return a CompletableFuture containing the ResponseEntity
       */
      public CompletableFuture<ResponseEntity<?>> createTraining(TrainingRequestDTO trainingRequest, String token) {
            Optional<Trainee> traineeOpt = Optional.ofNullable(traineeService.findTraineeByUsername(trainingRequest.getTraineeUsername()));
            if (traineeOpt.isEmpty()) {
                  return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.TRAINEE_NOT_FOUND));
            }

            Optional<Trainer> trainerOpt = Optional.ofNullable(trainerService.findTrainerByUsername(trainingRequest.getTrainerUsername()));
            if (trainerOpt.isEmpty()) {
                  return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.TRAINER_NOT_FOUND));
            }

            Training training = createTrainingEntity(trainingRequest, traineeOpt.get(), trainerOpt.get());
            TrainingRequest request = mapToTrainingRequest(trainerOpt.get(), trainingRequest.getTrainingDate(), trainingRequest.getTrainingDuration(), "add");

            trainerWorkloadClient.trainingRequest(request, token);
            trainingService.createTraining(training);

            return CompletableFuture.completedFuture(ResponseEntity.ok().build());
      }

      /**
       * Deletes an existing training session.
       *
       * @param id the ID of the training session to delete
       * @param token the authorization token
       * @return a CompletableFuture containing the ResponseEntity
       */
      public CompletableFuture<ResponseEntity<?>> deleteTraining(Long id, String token) {
            Optional<Training> trainingOpt = Optional.ofNullable(trainingService.findById(id));
            if (trainingOpt.isEmpty()) {
                  return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.TRAINING_NOT_FOUND));
            }

            Training training = trainingOpt.get();
            TrainingRequest request = mapToTrainingRequest(training.getTrainer(), training.getTrainingDate(), training.getTrainingDuration(), "delete");

            trainerWorkloadClient.trainingRequest(request, token);
            trainingService.deleteTraining(id);

            return CompletableFuture.completedFuture(ResponseEntity.ok().build());
      }

      /**
       * Retrieves all unique training types.
       *
       * @return a set of unique training types
       */
      public Set<TrainingType> getUniqueTrainingTypes() {
            return trainingService.findAll().stream()
                    .map(Training::getTrainingType)
                    .collect(Collectors.toCollection(HashSet::new));
      }

      /**
       * Creates a Training entity from the given training request, trainee, and trainer.
       *
       * @param trainingRequest the training request data transfer object
       * @param trainee the trainee entity
       * @param trainer the trainer entity
       * @return the created Training entity
       */
      private Training createTrainingEntity(TrainingRequestDTO trainingRequest, Trainee trainee, Trainer trainer) {
            Training training = new Training();
            training.setTrainee(trainee);
            training.setTrainer(trainer);
            training.setTrainingName(trainingRequest.getTrainingName());
            training.setTrainingDate(trainingRequest.getTrainingDate());
            training.setTrainingDuration(trainingRequest.getTrainingDuration());
            TrainingType trainingType = new TrainingType();
            trainingType.setTypeName(trainingRequest.getTrainingName());
            training.setTrainingType(trainingType);
            return training;
      }

      /**
       * Maps the given trainer, training date, training duration, and action to a TrainingRequest.
       *
       * @param trainer the trainer entity
       * @param trainingDate the date of the training
       * @param trainingDuration the duration of the training
       * @param action the action type (add or delete)
       * @return the mapped TrainingRequest
       */
      private TrainingRequest mapToTrainingRequest(Trainer trainer, LocalDate trainingDate, int trainingDuration, String action) {
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
}