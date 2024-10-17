package com.uw.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/trainings")
public class TrainingController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TraineeService traineeService,
                              TrainerService trainerService, TrainingService trainingService){
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody TrainingRequestDTO trainingRequest
            ){

        Trainee trainee = traineeService.findTraineeByUsername(trainingRequest.getTraineeUsername());
        if (trainee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.TRAINEE_NOT_FOUND);
        }

        Trainer trainer = trainerService.findTrainerByUsername(trainingRequest.getTrainerUsername());
        if (trainer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.TRAINER_NOT_FOUND);
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
