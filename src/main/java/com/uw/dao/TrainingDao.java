package com.uw.dao;

import com.uw.model.Training;

public interface TrainingDao {
    public void create( Training training );
    public Training selectProfile( long id );
}
