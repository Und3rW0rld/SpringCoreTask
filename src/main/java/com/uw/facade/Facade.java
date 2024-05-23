package com.uw.facade;

import com.uw.service.TraineeService;
import com.uw.service.TrainerService;
import com.uw.service.TrainingService;
import com.uw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Facade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final UserService userService;

    @Autowired
    public Facade ( TraineeService traineeService, TrainingService trainingService, TrainerService trainerService, UserService userService ) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.userService = userService;
    }

    public TraineeService getTraineeService(){
        return this.traineeService;
    }

    public TrainerService getTrainerService(){
        return this.trainerService;
    }

    public TrainingService getTrainingService(){
        return this.trainingService;
    }

    public UserService getUserService(){ return this.userService; }
}
