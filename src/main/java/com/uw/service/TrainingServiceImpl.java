package com.uw.service;

import com.uw.dao.TrainingDao;
import com.uw.model.Training;
import com.uw.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService{

    private TrainingDao trainingDao;

    @Autowired
    public void setTrainingDao( TrainingDao trainingDao){
        this.trainingDao = trainingDao;
    }

    @Override
    public long createTraining(Training training) {
        trainingDao.create(training);
        return training.getId();
    }

    @Override
    public void deleteTraining(long id) {
        trainingDao.delete(id);
    }

    @Override
    public Training selectTrainingProfile(long id) throws Exception {
        Training training = this.trainingDao.selectProfile(id);
        if(training == null){
            throw new Exception("The id specified is not in the list of profiles");
        }
        return training;
    }

    @Override
    public List<Training> findAll() {
        return trainingDao.findAll();
    }

    @Override
    public List<Training> getTrainerTrainings(String username, LocalDate startDate, LocalDate endDate, String traineeName) {
        return trainingDao.getTrainerTrainings(username, startDate, endDate, traineeName);
    }

    @Override
    public List<Training> getTraineeTrainings(String username, LocalDate startDate, LocalDate endDate, String trainerName, TrainingType trainingType) {
        return trainingDao.getTraineeTrainings(username, startDate, endDate, trainerName, trainingType);
    }

    @Override
    public Training findById(Long id) {
        return trainingDao.findById(id);
    }
}
