package com.uw.dao;

import com.uw.model.Trainee;

public interface TraineeDao {
    void create( Trainee trainee );
    void update( Trainee trainee );
    void delete( long id );
    Trainee selectProfile (long id);
}
