package com.uw.service;

import com.uw.model.Trainee;
import com.uw.model.Trainer;

import java.util.List;

public interface TraineeService {
    long createTrainee(Trainee trainee );
    void updateTrainee(Trainee trainee);
    void deleteTrainee( long id ) throws Exception;
    Trainee selectTraineeProfile( long id ) throws Exception;
    List<Trainee> findAll();
    Trainee existTraineeByUserId( long id );
    Trainee findTraineeByUsername(String username);
    boolean setIsActive(Trainee trainee);
    void deleteTraineeByUserName( String username);
    List<Trainer> getTrainers(Trainee trainee);
}
