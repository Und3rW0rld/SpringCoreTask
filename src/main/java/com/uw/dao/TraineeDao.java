package com.uw.dao;

import com.uw.model.Trainee;
import com.uw.model.Trainer;

import java.util.List;

public interface TraineeDao {
    void create( Trainee trainee );
    void update( Trainee trainee );
    void delete( long id );
    Trainee selectProfile (long id);
    List<Trainee> findAll();
    Trainee existTraineeByUserId(long id);
    Trainee findTraineeByUsername(String username);
    boolean activeDeActiveProfile(Trainee trainee);
    void deleteTraineeByUsername(String username);
    List<Trainer> findTrainers(Trainee trainee);
}
