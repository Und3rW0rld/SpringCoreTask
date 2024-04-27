package com.uw.service;

import com.uw.dao.TraineeDao;
import com.uw.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraineeServiceImpl implements TraineeService{

    private TraineeDao traineeDao;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao){
        this.traineeDao = traineeDao;
    }

    @Override
    public long createTrainee(Trainee trainee) {
        this.traineeDao.create(trainee);
        return trainee.getUserId();
    }

    @Override
    public void updateTrainee(Trainee trainee) {
        this.traineeDao.update(trainee);
    }

    @Override
    public void deleteTrainee(long id) throws Exception {
        if(this.traineeDao.selectProfile(id) == null){
            throw new Exception("The id specified is not in the list of profiles");
        }
        this.traineeDao.delete(id);
    }

    @Override
    public Trainee selectTraineeProfile(long id) throws Exception {
        if(this.traineeDao.selectProfile(id) == null){
            throw new Exception("The id specified is not in the list of profiles");
        }
        return this.traineeDao.selectProfile(id);
    }
}
