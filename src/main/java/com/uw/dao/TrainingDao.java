package com.uw.dao;

import com.uw.model.Training;

public interface TrainingDao {
    void create( Training training );
    Training selectProfile( long id );
}
