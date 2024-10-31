package com.uw.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingRequest {

      private String trainerUsername;
      private String firstName;
      private String lastName;
      private boolean isActive;
      private LocalDate trainingDate;
      private int trainingDuration; // duration in minutes or hours, depending on your preference
      private String actionType; // "ADD" or "DELETE"
}
