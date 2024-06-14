package com.uw.dto;

import java.util.List;

public class UpdateTrainersDTO {
    private String traineeUsername;
    private List<String> trainerUsernames;

    public UpdateTrainersDTO(String traineeUsername, List<String> trainerUsernames) {
        this.traineeUsername = traineeUsername;
        this.trainerUsernames = trainerUsernames;
    }

    public UpdateTrainersDTO() {

    }

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }

    public List<String> getTrainerUsernames() {
        return trainerUsernames;
    }

    public void setTrainerUsernames(List<String> trainerUsernames) {
        this.trainerUsernames = trainerUsernames;
    }
}
