package com.uw.service;

import com.uw.model.User;
import com.uw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Autowired
    public AuthService(UserRepository userRepository, TraineeService traineeService, TrainerService trainerService) {
        this.userRepository = userRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    public boolean isTrainee(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        User existing;
        if (user.isPresent()){
            existing = user.get();
        }else{
            throw new IllegalStateException("No user with the given username");
        }
        return traineeService.existTraineeByUserId(existing.getId()) != null;
    }

    public boolean isTrainer(String username, String password) {
        return trainerService.findTrainerByUsername(username) != null;
    }

    public static String generateCredentials(String username, String password){
        String credentials = username+"-"+password;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    public static String[] decodeCredentials(String credentials){
        byte[] decodedBytes = Base64.getDecoder().decode(credentials);
        String decodedCredentials = new String(decodedBytes);
        // Dividir las credenciales en nombre de usuario y contraseña
        return decodedCredentials.split("-");
    }

}
