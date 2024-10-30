package com.uw.service;

import com.uw.model.Training;
import com.uw.model.TrainingType;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing training operations.
 */
public interface TrainingService {

    /**
     * Creates a new training session.
     *
     * @param training the training entity to be created
     * @return the ID of the created training
     */
    long createTraining(Training training);

    /**
     * Selects the training profile by ID.
     *
     * @param id the ID of the training profile to select
     * @return the selected training profile
     * @throws Exception if the training profile is not found
     */
    Training selectTrainingProfile(long id) throws Exception;

    /**
     * Retrieves all training sessions.
     *
     * @return a list of all training sessions
     */
    List<Training> findAll();

    /**
     * Deletes a training session by ID.
     *
     * @param id the ID of the training session to delete
     */
    void deleteTraining(long id);

    /**
     * Retrieves training sessions for a specific trainer within a date range.
     *
     * @param username the username of the trainer
     * @param startDate the start date of the training sessions
     * @param endDate the end date of the training sessions
     * @param traineeName the name of the trainee
     * @return a list of training sessions for the specified trainer
     */
    List<Training> getTrainerTrainings(String username, LocalDate startDate, LocalDate endDate, String traineeName);

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
    List<Training> getTraineeTrainings(String username, LocalDate startDate, LocalDate endDate, String trainerName, TrainingType trainingType);

    /**
     * Finds a training session by ID.
     *
     * @param id the ID of the training session to find
     * @return the found training session, or null if not found
     */
    Training findById(Long id);
}