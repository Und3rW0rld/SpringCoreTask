package com.uw.service;

import com.uw.model.Training;

public interface TrainingService {
    long createTraining(Training training);
    Training selectTrainingProfile( long id ) throws Exception;
}
