package com.uw.repository;

import com.uw.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
      Training findByTrainingName( String trainingName );
}
