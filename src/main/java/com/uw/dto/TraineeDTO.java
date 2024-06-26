package com.uw.dto;

import java.time.LocalDate;
import java.util.List;

public class TraineeDTO {

    private long id;
    private LocalDate dateOfBirth;
    private String address;
    private String firstName;
    private String lastName;
    private String username;
    private List<TrainerDTO> trainers;
    private boolean isActive;

    public TraineeDTO(){

    }

    public List<TrainerDTO> getTrainers() {
        return trainers;
    }

    public TraineeDTO(LocalDate dateOfBirth, String address) {
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    public void setTrainers(List<TrainerDTO> trainers) {
        this.trainers = trainers;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
