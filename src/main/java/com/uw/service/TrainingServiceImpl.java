package com.uw.service;

import com.uw.dao.TrainingDao;
import com.uw.model.Training;
import com.uw.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of the TrainingService interface.
 * Provides methods for managing training sessions.
 */
@Service
public class TrainingServiceImpl implements TrainingService {

    private TrainingDao trainingDao;

    /**
     * Sets the TrainingDao instance.
     *
     * @param trainingDao the TrainingDao instance to set
     */
    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    /**
     * Creates a new training session.
     *
     * @param training the training entity to be created
     * @return the ID of the created training
     */
    @Override
    public long createTraining(Training training) {
        trainingDao.create(training);
        return training.getId();
    }

    /**
     * Deletes a training session by ID.
     *
     * @param id the ID of the training session to delete
     */
    @Override
    public void deleteTraining(long id) {
        trainingDao.delete(id);
    }

    /**
     * Selects the training profile by ID.
     *
     * @param id the ID of the training profile to select
     * @return the selected training profile
     * @throws Exception if the training profile is not found
     */
    @Override
    public Training selectTrainingProfile(long id) throws Exception {
        Training training = this.trainingDao.selectProfile(id);
        if (training == null) {
            throw new Exception("The id specified is not in the list of profiles");
        }
        return training;
    }

    /**
     * Retrieves all training sessions.
     *
     * @return a list of all training sessions
     */
    @Override
    public List<Training> findAll() {
        return trainingDao.findAll();
    }

    /**
     * Retrieves training sessions for a specific trainer within a date range.
     *
     * @param username the username of the trainer
     * @param startDate the start date of the training sessions
     * @param endDate the end date of the training sessions
     * @param traineeName the name of the trainee
     * @return a list of training sessions for the specified trainer
     */
    @Override
    public List<Training> getTrainerTrainings(String username, LocalDate startDate, LocalDate endDate, String traineeName) {
        return trainingDao.getTrainerTrainings(username, startDate, endDate, traineeName);
    }

    /**
     * Retrieves training sessions for a specific trainee within a date range.
     *
     * @param username the username of the trainee
     * @param startDate the start date of the training sessions
     * @param endDate the end date of the training sessions
     * @param trainerName the name of the trainer
     * @param trainingType the type of training
     * @return a list of training sessions for the specified trainee
     */
    @Override
    public List<Training> getTraineeTrainings(String username, LocalDate startDate, LocalDate endDate, String trainerName, TrainingType trainingType) {
        return trainingDao.getTraineeTrainings(username, startDate, endDate, trainerName, trainingType);
    }

    /**
     * Finds a training session by ID.
     *
     * @param id the ID of the training session to find
     * @return the found training session, or null if not found
     */
    @Override
    public Training findById(Long id) {
        return trainingDao.findById(id);
    }
}