package com.uw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingRequestDTO {
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private LocalDate trainingDate;
    private int trainingDuration;
}
