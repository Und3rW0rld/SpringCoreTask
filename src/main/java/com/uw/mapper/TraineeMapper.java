package com.uw.mapper;

import com.uw.dto.TraineeDTO;
import com.uw.model.Trainee;
import com.uw.model.User;

import java.util.stream.Collectors;

public class TraineeMapper {
    public static TraineeDTO toDTO(Trainee trainee) {
        if (trainee == null) {
            return null;
        }

        TraineeDTO dto = new TraineeDTO();
        dto.setId(trainee.getId());
        dto.setFirstName(trainee.getUser().getFirstName());
        dto.setLastName(trainee.getUser().getLastName());
        dto.setUsername(trainee.getUser().getUsername());
        dto.setDateOfBirth(trainee.getDateOfBirth());
        dto.setAddress(trainee.getAddress());
        dto.setTrainers(trainee.getTrainers().stream()
                .map(TrainerMapper::toDTO)
                .collect(Collectors.toList()));
        dto.setActive(trainee.getUser().isActive());
        return dto;
    }

    public static Trainee toEntity(TraineeDTO traineeDTO, User user) {
        Trainee trainee = new Trainee();
        trainee.setId(traineeDTO.getId());
        trainee.setDateOfBirth(traineeDTO.getDateOfBirth());
        trainee.setAddress(traineeDTO.getAddress());
        trainee.setUser(user);
        // Los entrenadores se asignarán por separado
        return trainee;
    }
}
