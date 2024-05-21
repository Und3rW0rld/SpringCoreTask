package com.uw.dao;

import com.uw.model.Trainer;

public interface TrainerDao {
    void create( Trainer trainer );
    void update( Trainer trainer );
    Trainer selectProfile(long id);
}
