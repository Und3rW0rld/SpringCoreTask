package com.uw.controller;

import com.uw.dto.TraineeDTO;
import com.uw.dto.TrainerDTO;
import com.uw.dto.TrainingDTO;
import com.uw.dto.UpdateTrainersDTO;
import com.uw.mapper.TraineeMapper;
import com.uw.mapper.TrainerMapper;
import com.uw.metrics.UserMetrics;
import com.uw.model.Trainee;
import com.uw.model.Trainer;
import com.uw.model.User;
import com.uw.model.TrainingType;
import com.uw.service.TraineeService;
import com.uw.service.TrainerService;
import com.uw.service.TrainingService;
import com.uw.util.ErrorMessages;
import com.uw.util.PasswordGenerator;
import com.uw.util.UserNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/trainees")
public class TraineeController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final UserNameGenerator userNameGenerator;
    private final PasswordGenerator passwordGenerator;
    private final TrainingService trainingService;
    private UserMetrics userMetrics;

    public TraineeController(TraineeService traineeService, TrainerService trainerService, UserNameGenerator userNameGenerator,
                             PasswordGenerator passwordGenerator,
                             TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.userNameGenerator = userNameGenerator;
        this.passwordGenerator = passwordGenerator;
        this.trainingService = trainingService;
    }



    @Autowired
    public void setUserMetrics(UserMetrics userMetrics) {
        this.userMetrics = userMetrics;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getTraineeProfile(
            @PathVariable("username") String username){
        Trainee trainee = traineeService.findTraineeByUsername(username);

        TraineeDTO traineeProfileDTO = TraineeMapper.toDTO(trainee);

        // Retornar la respuesta con el perfil del trainee
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(traineeProfileDTO);

    }

    @PostMapping
    public ResponseEntity<String[]> create(@RequestBody TraineeDTO traineeDTO){
        //Validar entradas
        if( traineeDTO.getFirstName() == null || traineeDTO.getLastName() == null){
            return ResponseEntity.badRequest().body(new String[]{"Error: Both 'firstName' and 'lastName' are required fields"});
        }
        String firstName = traineeDTO.getFirstName();
        String lastName = traineeDTO.getLastName();
        String username = userNameGenerator.generateUsername(firstName, lastName);
        String password = passwordGenerator.generatePassword(10);
        User user = new User();
        user.setActive(true);
        user.setPassword(password);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        Trainee trainee = TraineeMapper.toEntity(traineeDTO, user);

        trainee.setUser(user);
        traineeService.createTrainee(trainee);
        userMetrics.incrementUserRegistrationCount();
        return ResponseEntity.ok(new String[]{username, password});
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> update(
            @RequestBody TraineeDTO traineeDTO,
            @PathVariable("username") String username){

        Trainee traineeToUpdate = traineeService.findTraineeByUsername(username);
        if (traineeToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.TRAINEE_NOT_FOUND);
        }

        // Actualizar solo los campos no nulos del DTO
        if (traineeDTO.getFirstName() != null) {
            traineeToUpdate.getUser().setFirstName(traineeDTO.getFirstName());
        }
        if (traineeDTO.getLastName() != null) {
            traineeToUpdate.getUser().setLastName(traineeDTO.getLastName());
        }
        // Actualizar dateOfBirth solo si se proporciona un valor en el DTO
        if (traineeDTO.getDateOfBirth() != null) {
            traineeToUpdate.setDateOfBirth(traineeDTO.getDateOfBirth());
        }
        // Actualizar la dirección solo si se proporciona un valor en el DTO
        if (traineeDTO.getAddress() != null) {
            traineeToUpdate.setAddress(traineeDTO.getAddress());
        }
        String newUsername = userNameGenerator.generateUsername(traineeToUpdate.getUser().getFirstName(), traineeToUpdate.getUser().getLastName());
        if (newUsername.toLowerCase().matches(traineeToUpdate.getUser().getUsername().toLowerCase()+"\\d*")){
            newUsername = traineeToUpdate.getUser().getUsername();
        }
        traineeToUpdate.getUser().setUsername(newUsername);

        traineeService.updateTrainee(traineeToUpdate);
        return ResponseEntity.ok().body(traineeToUpdate);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> delete(@PathVariable("username") String username){

        Trainee traineeToDelete = traineeService.findTraineeByUsername(username);
        if (traineeToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.TRAINEE_NOT_FOUND);
        }

        traineeService.deleteTraineeByUserName(username);

        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
    }

    @GetMapping("/{username}/not-assigned-trainers")
    public ResponseEntity <?> getNotAssignedActiveTrainers(
            @PathVariable("username") String username) {

        Trainee trainee = traineeService.findTraineeByUsername(username);

        List<Trainer> trainers = trainerService.findAll();
        List<Trainer> results = new ArrayList<>();
        for (Trainer trainer : trainers){
            boolean find = false;
            for (Trainer t : trainee.getTrainers()){
                if (t.getId() == trainer.getId() ){
                    find = true;
                    break;
                }
            }
            if(!find && trainer.getUser().isActive()){
                results.add(trainer);
            }
        }

        List<TrainerDTO> trainerDTOs = results.stream().map(TrainerMapper::toDTO).toList();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(trainerDTOs);
    }

    @PutMapping("/{username}/trainers")
    public ResponseEntity<?> updateTraineeTrainers(
            @RequestBody UpdateTrainersDTO updateTrainersDTO,
            @PathVariable("username") String username
    ){

        Trainee trainee = traineeService.findTraineeByUsername(username);

        List<Trainer> trainers = new ArrayList<>();

        for(String u: updateTrainersDTO.getTrainerUsernames()){
            Trainer trainer = trainerService.findTrainerByUsername(u);
            if(trainer != null){
                trainers.add(trainer);
            }
        }

        trainee.setTrainers(trainers);
        traineeService.updateTrainee(trainee);

        // Mapear entrenadores a DTOs
        List<TrainerDTO> trainerDTOs = trainers.stream()
                .map(TrainerMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(trainerDTOs);
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<?> getTraineeTrainings(
            @PathVariable("username") String username,
            @RequestParam(value = "periodFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
            @RequestParam(value = "periodTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
            @RequestParam(value = "trainerName", required = false) String trainerName,
            @RequestParam(value = "trainingType", required = false) String trainingType) {

        TrainingType trainingType1 = new TrainingType();
        if(trainingType != null){
            trainingType1.setTypeName(trainingType);
        }
        List<TrainingDTO> trainings = trainingService.getTraineeTrainings(username, periodFrom, periodTo, trainerName, trainingType1).stream()
                .map(training -> {
                    TrainingDTO dto = new TrainingDTO();
                    dto.setTrainingName(training.getTrainingName());
                    dto.setTrainingDate(training.getTrainingDate());
                    dto.setTrainingType(training.getTrainingType().getTypeName().name());
                    dto.setTrainingDuration(training.getTrainingDuration());
                    dto.setTrainerName(training.getTrainer().getUser().getFirstName()+" "+training.getTrainer().getUser().getLastName());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(trainings);
    }

    @PatchMapping("/{username}/status")
    public ResponseEntity<?> updateTraineeStatus(
            @PathVariable("username") String username,
            @RequestParam("active") boolean active) {

        Trainee trainee = traineeService.findTraineeByUsername(username);
        if (trainee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.TRAINEE_NOT_FOUND);
        }

        trainee.getUser().setActive(active);
        traineeService.updateTrainee(trainee);

        return ResponseEntity.ok().build();
    }

}
