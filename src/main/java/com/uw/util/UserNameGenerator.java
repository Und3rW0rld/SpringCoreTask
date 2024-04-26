package com.uw.util;

import com.uw.dao.TraineeDao;
import com.uw.dao.TraineeDaoImpl;
import com.uw.dao.TrainerDao;
import com.uw.dao.TrainerDaoImpl;
import com.uw.model.Trainee;
import com.uw.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserNameGenerator {

    private TraineeDao traineeDao;
    private TrainerDao trainerDao;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public String generateUsername( String firstName, String lastName ) {
        String username = firstName + "." + lastName;
        int cont = 0;
        StorageImpl s = (StorageImpl) ((TraineeDaoImpl) traineeDao).getStorage();
        for( Object value: s.getMyStorage().values().toArray() ){
            Trainee trainee = (Trainee) value;
            if( trainee.getUsername().equals(username)){
                cont++;
            }
        }
        s = (StorageImpl) ((TrainerDaoImpl) trainerDao).getStorage();
        for( Object value: s.getMyStorage().values().toArray() ){
            Trainer trainer = (Trainer) value;
            if( trainer.getUsername().equals(username)){
                cont++;
            }
        }
        if ( cont == 0 ){
            return username;
        }else{
            return username+cont;
        }
    }
}
