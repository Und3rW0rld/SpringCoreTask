package com.uw.service;

import com.uw.model.Training;

public interface TrainingService {
    public long createTraining(Training training);
    public Training selectTrainingProfile( long id ) throws Exception;
}
