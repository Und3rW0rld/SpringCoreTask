package com.uw.dao;

import com.uw.model.Training;
import com.uw.model.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface TrainingDao {
    void create( Training training );
    Training selectProfile( long id );
    List<Training> findAll();
    List<Training> getTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType);

    List<Training> getTrainerTrainings(String username, LocalDate startDate, LocalDate endDate, String traineeName);
}
