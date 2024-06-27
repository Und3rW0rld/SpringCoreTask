package com.uw.service;

import com.uw.model.Trainer;
import com.uw.model.User;

import java.util.List;

public interface TrainerService {
    long createTrainer(Trainer trainer);
    void updateTrainer(Trainer trainer);
    Trainer selectTrainerProfile( long id ) throws Exception;
    List<Trainer> findAll();
    Trainer getTrainerByUser(User user);
    Trainer findTrainerByUsername(String username);
}
