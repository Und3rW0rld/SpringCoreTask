package com.uw.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name= "TRAINING")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Trainee trainee;

    @ManyToOne(cascade = CascadeType.ALL)
    private Trainer trainer;

    @Column(name="TRAINING_NAME")
    private String trainingName;

    @ManyToOne(cascade = CascadeType.ALL)
    private TrainingType trainingType;

    @Column(name="TRAINING_DATE")
    private LocalDate trainingDate;

    @Column(name="TRAINING_DURATION")
    private int trainingDuration;

    public Training(){

    }

    public Training(String trainingName, TrainingType trainingType, LocalDate trainingDate, int trainingDuration) {
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public int getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(int trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", trainee_id=" + trainee.getId() +
                ", trainer_id=" + trainer.getId() +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType.getTypeName() +
                ", trainingDate=" + trainingDate +
                ", trainingDuration='" + trainingDuration + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return id == training.id && Objects.equals(trainee, training.trainee) && Objects.equals(trainer, training.trainer) && Objects.equals(trainingName, training.trainingName) && Objects.equals(trainingType, training.trainingType) && Objects.equals(trainingDate, training.trainingDate) && Objects.equals(trainingDuration, training.trainingDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trainee, trainer, trainingName, trainingType, trainingDate, trainingDuration);
    }
}
