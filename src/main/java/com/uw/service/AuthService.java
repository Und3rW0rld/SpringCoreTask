package com.uw.service;

import com.uw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthService {

    private final UserService userService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Autowired
    public AuthService(UserService userService, TraineeService traineeService, TrainerService trainerService) {
        this.userService = userService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    public boolean authentication(String username, String password){
        if (!userService.validateUsername(username)) {
            // El nombre de usuario no se encuentra en la base de datos
            return false;
        }
        // Verificar si la contraseña es correcta
        return userService.validatePassword(username, password);
    }

    public boolean isTrainee(String username, String password) {
        if(!this.authentication(username, password)){
            return false;
        }
        User user = userService.findUserByUsername(username);
        return traineeService.existTraineeByUserId(user.getId()) != null;
    }

    public boolean isTrainer(String username, String password) {
        if(!this.authentication(username, password)){
            return false;
        }
        User user = userService.findUserByUsername(username);
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
