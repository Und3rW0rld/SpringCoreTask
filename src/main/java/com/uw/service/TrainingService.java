package com.uw.service;

import com.uw.model.Training;
import com.uw.model.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {
    long createTraining(Training training);
    Training selectTrainingProfile( long id ) throws Exception;
    List<Training> findAll();
      void deleteTraining(long id);
    List<Training> getTrainerTrainings(String username, LocalDate startDate, LocalDate endDate, String traineeName);

    List<Training> getTraineeTrainings(String username, LocalDate startDate, LocalDate endDate, String trainerName, TrainingType trainingType);

    Training findById(Long id);
}
