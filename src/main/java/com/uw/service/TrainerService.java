package com.uw.service;

import com.uw.model.Trainer;

public interface TrainerService {
    public void createTrainer(Trainer trainer);
    public void updateTrainer(Trainer trainer);
    public Trainer selectTrainerProfile( long id ) throws Exception;

}
