package com.uw.dao;

import com.uw.model.Trainee;

public interface TraineeDao {
    public void create( Trainee trainee );
    public void update( Trainee trainee );
    public void delete( long id );
    public Trainee selectProfile (long id);
}
