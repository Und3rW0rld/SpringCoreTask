package com.uw.dao;

import com.uw.model.Trainee;
import com.uw.util.Storage;
import com.uw.util.StorageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDaoImpl implements TraineeDao{

    private Storage storage;

    @Autowired
    public void setStorage( Storage storage ){
        this.storage = storage;
    }

    @Override
    public void create(Trainee trainee) {
        long id = this.storage.getSize() + 1;
        trainee.setUserId( id );
        this.storage.save( trainee.getUserId(), trainee );
    }

    @Override
    public void update(Trainee trainee) {
        this.storage.save( trainee.getUserId(), trainee );
    }

    @Override
    public void delete(long id) {
        this.storage.remove( id );
    }

    @Override
    public Trainee selectProfile(long id) {
        return ( Trainee ) this.storage.get( id );
    }

    public Storage getStorage() {
        return this.storage;
    }
}
