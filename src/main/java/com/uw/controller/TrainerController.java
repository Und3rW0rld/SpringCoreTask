package com.uw.controller;

import com.uw.dto.TrainerDTO;
import com.uw.dto.TrainingDTO;
import com.uw.mapper.TrainerMapper;
import com.uw.model.*;
import com.uw.service.AuthService;
import com.uw.service.TrainerService;
import com.uw.service.TrainingService;
import com.uw.util.PasswordGenerator;
import com.uw.util.UserNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/trainers")
public class TrainerController {

    private final TrainerService trainerService;
    private final UserNameGenerator userNameGenerator;
    private final PasswordGenerator passwordGenerator;
    private final AuthService authService;
    private final TrainingService trainingService;

    @Autowired
    public TrainerController(TrainerService trainerService, UserNameGenerator userNameGenerator,
                             PasswordGenerator passwordGenerator, AuthService authService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.userNameGenerator = userNameGenerator;
        this.passwordGenerator = passwordGenerator;
        this.authService = authService;
        this.trainingService = trainingService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getTraineeProfile(
            @PathVariable("username") String username,
            @RequestHeader(value = "Authorization") String auth){

        String[] credentials = AuthService.decodeCredentials(auth);
        if (credentials.length != 2) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
        }

        String providedUsername = credentials[0];
        String providedPassword = credentials[1];

        // Autenticar el usuario
        boolean isAuthenticated = authService.authentication(providedUsername, providedPassword);
        if (!isAuthenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        if (!authService.isTrainer(providedUsername, providedPassword)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainer not found");
        }

        Trainer trainer = trainerService.findTrainerByUsername(username);

        TrainerDTO trainerProfileDTO = TrainerMapper.toDTO(trainer);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(trainerProfileDTO);

    }

    @PostMapping
    public ResponseEntity<String[]> create(@RequestBody TrainerDTO trainerDTO) {
        // Validar entrada
        if (trainerDTO.getSpecialization() == null || trainerDTO.getFirstName() == null || trainerDTO.getLastName() == null) {
            return ResponseEntity.badRequest().body(new String[]{"Error: Missing required fields"});
        }

        TrainingType specialization = new TrainingType();
        specialization.setTypeName(trainerDTO.getSpecialization());
        if (specialization.getTypeName() == null) {
            return ResponseEntity.badRequest().body(new String[]{"Error: Invalid specialization"});
        }

        // Crear usuario
        String username = userNameGenerator.generateUsername(trainerDTO.getFirstName(), trainerDTO.getLastName());
        String password = passwordGenerator.generatePassword(10);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(trainerDTO.getFirstName());
        user.setLastName(trainerDTO.getLastName());
        user.setActive(true);
        // Crear entrenador
        Trainer trainer = TrainerMapper.toEntity(trainerDTO, specialization, user);
        trainer.setUser(user);
        trainerService.createTrainer(trainer); // Guardar entrenador

        // Responder con nombre de usuario y contraseña
        return ResponseEntity.ok(new String[]{username, password});
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> update(
            @RequestBody TrainerDTO trainerDTO,
            @PathVariable("username") String username,
            @RequestHeader(value = "Authorization") String auth){

        String[] credentials = AuthService.decodeCredentials(auth);
        if (credentials.length != 2) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
        }

        String providedUsername = credentials[0];
        String providedPassword = credentials[1];

        // Autenticar el usuario
        boolean isAuthenticated = authService.authentication(providedUsername, providedPassword);
        if (!isAuthenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        if (!authService.isTrainer(providedUsername, providedPassword)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainer not found");
        }

        Trainer trainerToUpdate = trainerService.findTrainerByUsername(username);
        if (trainerToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainer not found");
        }

        // Actualizar solo los campos no nulos del DTO
        if (trainerDTO.getFirstName() != null) {
            trainerToUpdate.getUser().setFirstName(trainerDTO.getFirstName());
        }
        if (trainerDTO.getLastName() != null) {
            trainerToUpdate.getUser().setLastName(trainerDTO.getLastName());
        }
        // Actualizar dateOfBirth solo si se proporciona un valor en el DTO
        if (trainerDTO.getSpecialization() != null) {
            TrainingType trainingType = new TrainingType();
            trainingType.setTypeName(trainerDTO.getSpecialization());
            trainerToUpdate.setSpecialization(trainingType);
        }

        String newUsername = userNameGenerator.generateUsername(trainerToUpdate.getUser().getFirstName(), trainerToUpdate.getUser().getLastName());
        if (newUsername.toLowerCase().matches(trainerToUpdate.getUser().getUsername().toLowerCase()+"\\d*")){
            newUsername = trainerToUpdate.getUser().getUsername();
        }
        trainerToUpdate.getUser().setUsername(newUsername);
        trainerService.updateTrainer(trainerToUpdate);

        return ResponseEntity.ok().body(trainerToUpdate);
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<?> getTrainerTrainings(
            @PathVariable("username") String username,
            @RequestParam(value = "periodFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
            @RequestParam(value = "periodTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
            @RequestParam(value = "traineeName", required = false) String traineeName,
            @RequestHeader(value = "Authorization") String auth){
        // Decodificar y verificar las credenciales
        String[] credentials = AuthService.decodeCredentials(auth);
        if (credentials.length != 2) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
        }

        String providedUsername = credentials[0];
        String providedPassword = credentials[1];

        // Autenticar el usuario
        boolean isAuthenticated = authService.authentication(providedUsername, providedPassword);
        if (!isAuthenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        if (!authService.isTrainer(providedUsername, providedPassword)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainer not found");
        }

        List<Training> trainings = trainingService.getTrainerTrainings(username, periodFrom, periodTo, traineeName);
        List<TrainingDTO> trainingDTOs = trainings.stream()
                .map(training -> {
                    TrainingDTO dto = new TrainingDTO();
                    dto.setTrainingName(training.getTrainingName());
                    dto.setTrainingDate(training.getTrainingDate());
                    dto.setTrainingType(training.getTrainingType().getTypeName().name());
                    dto.setTrainingDuration(training.getTrainingDuration());
                    dto.setTraineeName(training.getTrainee().getUser().getFirstName() + " " + training.getTrainee().getUser().getLastName());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(trainingDTOs);
    }

    @PatchMapping("/{username}/status")
    public ResponseEntity<?> updateTraineeStatus(
            @RequestHeader(value = "Authorization") String auth,
            @PathVariable("username") String username,
            @RequestParam("active") boolean active) {

        String[] credentials = AuthService.decodeCredentials(auth);
        if (credentials.length != 2) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
        }

        String providedUsername = credentials[0];
        String providedPassword = credentials[1];

        // Autenticar el usuario
        boolean isAuthenticated = authService.authentication(providedUsername, providedPassword);
        if (!isAuthenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        Trainer trainer = trainerService.findTrainerByUsername(username);
        if (trainer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainer not found");
        }

        trainer.getUser().setActive(active);
        trainerService.updateTrainer(trainer);

        return ResponseEntity.ok().build();
    }

}
