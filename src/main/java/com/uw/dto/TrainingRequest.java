package com.uw.dto;
import java.time.LocalDate;

public class TrainingRequest {

      private String trainerUsername;
      private String firstName;
      private String lastName;
      private boolean isActive;
      private LocalDate trainingDate;
      private int trainingDuration; // duration in minutes or hours, depending on your preference
      private String actionType; // "ADD" or "DELETE"

      // Constructors
      public TrainingRequest() {
      }

      public TrainingRequest(String trainerUsername, String firstName, String lastName, boolean isActive, LocalDate trainingDate, int trainingDuration, String actionType) {
            this.trainerUsername = trainerUsername;
            this.firstName = firstName;
            this.lastName = lastName;
            this.isActive = isActive;
            this.trainingDate = trainingDate;
            this.trainingDuration = trainingDuration;
            this.actionType = actionType;
      }

      // Getters and Setters
      public String getTrainerUsername() {
            return trainerUsername;
      }

      public void setTrainerUsername(String trainerUsername) {
            this.trainerUsername = trainerUsername;
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

      public boolean isActive() {
            return isActive;
      }

      public void setActive(boolean active) {
            isActive = active;
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

      public String getActionType() {
            return actionType;
      }

      public void setActionType(String actionType) {
            this.actionType = actionType;
      }
}
