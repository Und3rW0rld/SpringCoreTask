package com.uw.dto;

import java.util.List;

public class TrainerDTO {

    private long id;
    private String specialization; // Assuming TrainingType has a name or description field
    private String username;
    private String firstName;
    private String lastName;
    private List<TraineeDTO> trainees; // Assuming you also have a TraineeDTO
    private boolean active;

    public TrainerDTO() {
    }

    public TrainerDTO(long id, String specialization, String username, String firstName, String lastName, List<TraineeDTO> trainees, boolean active) {
        this.id = id;
        this.specialization = specialization;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.trainees = trainees;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<TraineeDTO> getTrainees() {
        return trainees;
    }

    public void setTrainees(List<TraineeDTO> trainees) {
        this.trainees = trainees;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return """
           TrainerDTO{
               id=%d,
               specialization='%s',
               username='%s',
               firstName='%s',
               lastName='%s',
               trainees=%s
           }
           """.formatted(id, specialization, username, firstName, lastName, trainees);
    }

}
