package com.uw.service;

import com.uw.model.Trainee;

public interface TraineeService {
    long createTrainee(Trainee trainee );
    void updateTrainee(Trainee trainee);
    void deleteTrainee( long id ) throws Exception;
    Trainee selectTraineeProfile( long id ) throws Exception;
}
