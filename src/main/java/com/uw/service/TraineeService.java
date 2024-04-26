package com.uw.service;

import com.uw.dao.TrainerDao;
import com.uw.model.Trainee;

public interface TraineeService {
    public void createTrainee(Trainee trainee );
    public void updateTrainee(Trainee trainee);
    public void deleteTrainee( long id ) throws Exception;
    public Trainee selectTraineeProfile( long id ) throws Exception;
}
