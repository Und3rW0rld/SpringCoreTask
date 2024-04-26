package com.uw.service;

import com.uw.dao.TrainingDao;
import com.uw.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingServiceImpl implements TrainingService{

    private TrainingDao trainingDao;

    @Autowired
    public void setTrainingDao( TrainingDao trainingDao){
        this.trainingDao = trainingDao;
    }

    @Override
    public void createTraining(Training training) {
        trainingDao.create(training);
    }

    @Override
    public Training selectTrainingProfile(long id) throws Exception {
        if(this.trainingDao.selectProfile(id) == null){
            throw new Exception("The id specified is not in the list of profiles");
        }
        return trainingDao.selectProfile( id );
    }
}
