package com.uw.service;

import com.uw.model.Trainee;

public interface TraineeService {
    public long createTrainee(Trainee trainee );
    public void updateTrainee(Trainee trainee);
    public void deleteTrainee( long id ) throws Exception;
    public Trainee selectTraineeProfile( long id ) throws Exception;
}
