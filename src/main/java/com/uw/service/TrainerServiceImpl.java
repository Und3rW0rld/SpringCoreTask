package com.uw.service;

import com.uw.dao.TrainerDao;
import com.uw.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerServiceImpl implements TrainerService{

    private TrainerDao trainerDao;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao){
        this.trainerDao = trainerDao;
    }

    @Override
    public void createTrainer(Trainer trainer) {
        trainerDao.create(trainer);
    }

    @Override
    public void updateTrainer(Trainer trainer) {
        trainerDao.update(trainer);
    }

    @Override
    public Trainer selectTrainerProfile(long id) throws Exception {
        if(this.trainerDao.selectProfile(id) == null){
            throw new Exception("The id specified is not in the list of profiles");
        }
        return trainerDao.selectProfile(id);
    }
}
