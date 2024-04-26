package com.uw.dao;

import com.uw.model.Trainer;

public interface TrainerDao {
    public void create( Trainer trainer );
    public void update( Trainer trainer );
    public Trainer selectProfile(long id);
}
