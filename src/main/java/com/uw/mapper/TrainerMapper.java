package com.uw.mapper;

import com.uw.dto.TrainerDTO;
import com.uw.model.Trainer;
import com.uw.model.TrainingType;
import com.uw.model.User;

import java.util.stream.Collectors;

public class TrainerMapper {

    public static TrainerDTO toDTO(Trainer trainer) {
        if (trainer == null) {
            return null;
        }

        return new TrainerDTO(
                trainer.getId(),
                trainer.getSpecialization().getTypeName().name(),
                trainer.getUser().getUsername(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getTrainees().stream()
                        .map(TraineeMapper::toDTO)
                        .collect(Collectors.toList()),
                trainer.getUser().isActive()
        );
    }

    public static Trainer toEntity(TrainerDTO trainerDTO, TrainingType specialization, User user) {
        Trainer trainer = new Trainer();
        trainer.setSpecialization(specialization);
        trainer.setUser(user);
        return trainer;
    }

}
