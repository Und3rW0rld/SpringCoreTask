package com.uw.dao;

import com.uw.model.Training;
import com.uw.util.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDaoImpl implements TrainingDao{

    private Storage storage;

    @Autowired
    public void setStorage(Storage storage){
        this.storage = storage;
    }

    @Override
    public void create( Training training ) {

        long id = this.storage.getSize() + 1;
        training.setId(id);
        this.storage.save( training.getId(), training );

    }

    @Override
    public Training selectProfile(long id) {
        return (Training) this.storage.get(id);
    }
}
