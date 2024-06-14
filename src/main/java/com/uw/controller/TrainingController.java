package com.uw.controller;

import com.uw.dto.TrainingRequestDTO;
import com.uw.dto.TrainingTypeDTO;
import com.uw.model.Trainee;
import com.uw.model.Trainer;
import com.uw.model.Training;
import com.uw.model.TrainingType;
import com.uw.service.AuthService;
import com.uw.service.TraineeService;
import com.uw.service.TrainerService;
import com.uw.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/trainings")
public class TrainingController {

    private final AuthService authService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public TrainingController(AuthService authService,
                              TraineeService traineeService,
                              TrainerService trainerService, TrainingService trainingService){
        this.authService = authService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestHeader(value = "Authorization") String auth,
            @RequestBody TrainingRequestDTO trainingRequest
            ){
        String[] credentials = AuthService.decodeCredentials(auth);
        if (credentials.length != 2) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
        }

        String providedUsername = credentials[0];
        String providedPassword = credentials[1];

        // Autenticar el usuario
        boolean isAuthenticated = authService.authentication(providedUsername, providedPassword);
        if (!isAuthenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        Trainee trainee = traineeService.findTraineeByUsername(trainingRequest.getTraineeUsername());
        if (trainee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainee not found");
        }

        Trainer trainer = trainerService.findTrainerByUsername(trainingRequest.getTrainerUsername());
        if (trainer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainer not found");
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

        trainingService.createTraining(training);

        return ResponseEntity.ok().build();

    }

    @GetMapping("/types")
    public ResponseEntity<?> getTrainingTypes(){
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
}
