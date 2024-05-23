package com.uw.util;

import com.uw.model.Trainee;
import com.uw.model.Trainer;
import com.uw.service.TraineeService;
import com.uw.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserNameGenerator {

    private TraineeService traineeServiceImpl;
    private TrainerService trainerServiceImpl;

    @Autowired
    public void setTraineeService(TraineeService traineeServiceImpl) {
        this.traineeServiceImpl = traineeServiceImpl;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerServiceImpl) {
        this.trainerServiceImpl = trainerServiceImpl;
    }

    public String generateUsername( String firstName, String lastName ) {
        String username = firstName + "." + lastName;
        int cont = 0;
        if(!traineeServiceImpl.findAll().isEmpty()){
        for( Trainee value: traineeServiceImpl.findAll()){
            if( value.getUser().getUsername().equalsIgnoreCase(username)){
                cont++;
            }
        }
        }
        if(!trainerServiceImpl.findAll().isEmpty()){
        for( Trainer value: trainerServiceImpl.findAll()){
            if( value.getUser().getUsername().equalsIgnoreCase(username)){
                cont++;
            }
        }
        }
        if ( cont == 0 ){
            return username;
        }else{
            return username+cont;
        }
    }
}
