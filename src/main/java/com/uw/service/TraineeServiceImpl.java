package com.uw.service;

import com.uw.dao.TraineeDao;
import com.uw.model.Trainee;
import com.uw.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return trainee.getId();
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
        Trainee trainee = this.traineeDao.selectProfile(id);
        if(trainee == null){
            throw new Exception("The id specified is not in the list of profiles");
        }
        return trainee;
    }

    @Override
    public List<Trainee> findAll() {
        return traineeDao.findAll();
    }

    @Override
    public Trainee existTraineeByUserId(long id) {
        return traineeDao.existTraineeByUserId(id);
    }

    @Override
    public Trainee findTraineeByUsername(String username) {
        return traineeDao.findTraineeByUsername(username);
    }

    @Override
    public boolean setIsActive(Trainee trainee) {
        return traineeDao.activeDeActiveProfile(trainee);
    }

    @Override
    public void deleteTraineeByUserName(String username) {
        traineeDao.deleteTraineeByUsername(username);
    }

    @Override
    public List<Trainer> getTrainers( Trainee trainee) {
        return traineeDao.findTrainers(trainee);
    }

}
