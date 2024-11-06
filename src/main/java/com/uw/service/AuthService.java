package com.uw.service;

import com.uw.model.User;
import com.uw.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

/**
 * Service class for handling authentication-related operations.
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    /**
     * Constructor for AuthService.
     *
     * @param userRepository the UserRepository to be used for user operations
     * @param traineeService the TraineeService to be used for trainee operations
     * @param trainerService the TrainerService to be used for trainer operations
     */
    @Autowired
    public AuthService(UserRepository userRepository, TraineeService traineeService, TrainerService trainerService) {
        this.userRepository = userRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    /**
     * Checks if the user with the given username and password is a trainee.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return true if the user is a trainee, false otherwise
     */
    public boolean isTrainee(String username, String password) {
        logger.info("Checking if user {} is a trainee", username);
        Optional<User> user = userRepository.findByUsername(username);
        User existing;
        if (user.isPresent()) {
            existing = user.get();
        } else {
            logger.error("No user found with username {}", username);
            throw new IllegalStateException("No user with the given username");
        }
        return traineeService.existTraineeByUserId(existing.getId()) != null;
    }

    /**
     * Checks if the user with the given username and password is a trainer.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return true if the user is a trainer, false otherwise
     */
    public boolean isTrainer(String username, String password) {
        logger.info("Checking if user {} is a trainer", username);
        return trainerService.findTrainerByUsername(username) != null;
    }

    /**
     * Generates a Base64 encoded string of the given username and password.
     *
     * @param username the username to be encoded
     * @param password the password to be encoded
     * @return the Base64 encoded credentials
     */
    public static String generateCredentials(String username, String password) {
        String credentials = username + "-" + password;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    /**
     * Decodes the given Base64 encoded credentials into an array containing the username and password.
     *
     * @param credentials the Base64 encoded credentials
     * @return an array containing the username and password
     */
    public static String[] decodeCredentials(String credentials) {
        byte[] decodedBytes = Base64.getDecoder().decode(credentials);
        String decodedCredentials = new String(decodedBytes);
        return decodedCredentials.split("-");
    }
}