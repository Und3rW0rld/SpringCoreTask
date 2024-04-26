package com.uw.dao;

import com.uw.model.Trainer;
import com.uw.util.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDaoImpl implements TrainerDao{

    private Storage storage;

    @Autowired
    public void setStorage(Storage storage){
        this.storage = storage;
    }

    @Override
    public void create(Trainer trainer) {
        long id = this.storage.getSize() + 1;
        trainer.setUserId(id);
        this.storage.save( trainer.getUserId(), trainer );
    }

    @Override
    public void update(Trainer trainer) {
        this.storage.save( trainer.getUserId(), trainer );
    }

    @Override
    public Trainer selectProfile(long id) {
        return (Trainer) this.storage.get( id );
    }

    public Storage getStorage(){
        return this.storage;
    }
}
