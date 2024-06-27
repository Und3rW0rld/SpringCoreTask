package com.uw.service;

import com.uw.dao.TrainerDao;
import com.uw.model.Trainer;
import com.uw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService{

    private TrainerDao trainerDao;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao){
        this.trainerDao = trainerDao;
    }

    @Override
    public long createTrainer(Trainer trainer) {
        trainerDao.create(trainer);
        return trainer.getId();
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

    @Override
    public List<Trainer> findAll() {
        return trainerDao.findAll();
    }

    @Override
    public Trainer getTrainerByUser(User user) {
        return trainerDao.findTrainerByUser(user);
    }

    @Override
    public Trainer findTrainerByUsername(String username) {
        return trainerDao.findTrainerByUsername(username);
    }

}
