package com.uw.dao;

import com.uw.model.Trainer;
import com.uw.model.User;

import java.util.List;

public interface TrainerDao {
    void create( Trainer trainer );
    void update( Trainer trainer );
    Trainer selectProfile(long id);
    List<Trainer> findAll();
    Trainer findTrainerByUser(User user);
    Trainer findTrainerByUsername(String username);
}
