package com.uw.service;

import com.uw.model.Trainer;

public interface TrainerService {
    long createTrainer(Trainer trainer);
    void updateTrainer(Trainer trainer);
    Trainer selectTrainerProfile( long id ) throws Exception;
}
