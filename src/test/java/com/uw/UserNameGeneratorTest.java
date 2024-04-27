package com.uw;

import com.uw.dao.TraineeDao;
import com.uw.dao.TraineeDaoImpl;
import com.uw.dao.TrainerDao;
import com.uw.dao.TrainerDaoImpl;
import com.uw.model.Trainee;
import com.uw.model.Trainer;
import com.uw.util.StorageImpl;
import com.uw.util.UserNameGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UserNameGeneratorTest {

    private UserNameGenerator userNameGenerator;
    private TraineeDao traineeDao;
    private TrainerDao trainerDao;

    @Before
    public void setUp() {
        userNameGenerator = new UserNameGenerator();
        traineeDao = new TraineeDaoImpl();
        trainerDao = new TrainerDaoImpl();
        userNameGenerator.setTraineeDao(traineeDao);
        userNameGenerator.setTrainerDao(trainerDao);
    }

    @Test
    public void testGenerateUsername_NoDuplicates() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String expectedUsername = "John.Doe";
        StorageImpl storage = new StorageImpl();
        storage.setMyStorage(new HashMap<>());
        ((TraineeDaoImpl) traineeDao).setStorage(storage);
        ((TrainerDaoImpl) trainerDao).setStorage(storage);
        // Act
        String generatedUsername = userNameGenerator.generateUsername(firstName, lastName);

        // Assert
        assertEquals(expectedUsername, generatedUsername);
    }

    @Test
    public void testGenerateUsername_WithDuplicates() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String expectedUsername = "John.Doe1";

        // Simulate existing trainee with the same username
        Map<Long, Object> traineeStorage = new HashMap<>();
        Trainee trainee = new Trainee("John", "Doe", "John.Doe", "password", true, null, null);
        traineeStorage.put(1L, trainee);
        StorageImpl storageImpl = new StorageImpl();
        storageImpl.setMyStorage(traineeStorage);
        ((TraineeDaoImpl) traineeDao).setStorage(storageImpl);

        // Simulate empty trainer storage
        StorageImpl emptyStorage = new StorageImpl();
        emptyStorage.setMyStorage(new HashMap<>());
        ((TrainerDaoImpl) trainerDao).setStorage(emptyStorage);

        // Act
        String generatedUsername = userNameGenerator.generateUsername(firstName, lastName);

        // Assert
        assertEquals(expectedUsername, generatedUsername);
    }
    @Test
    public void testGenerateUsername_WithDuplicatesOnlyInTrainees() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String expectedUsername = "John.Doe1";

        // Simulate existing trainee with the same username
        Map<Long, Object> traineeStorage = new HashMap<>();
        Trainee trainee = new Trainee("John", "Doe", "John.Doe", "password", true, null, null);
        traineeStorage.put(1L, trainee);
        StorageImpl traineeStorageImpl = new StorageImpl();
        traineeStorageImpl.setMyStorage(traineeStorage);
        ((TraineeDaoImpl) traineeDao).setStorage(traineeStorageImpl);

        // Simulate empty trainer storage
        StorageImpl emptyStorage = new StorageImpl();
        emptyStorage.setMyStorage(new HashMap<>());
        ((TrainerDaoImpl) trainerDao).setStorage(emptyStorage);

        // Act
        String generatedUsername = userNameGenerator.generateUsername(firstName, lastName);

        // Assert
        assertEquals(expectedUsername, generatedUsername);
    }

    @Test
    public void testGenerateUsername_WithDuplicatesOnlyInTrainers() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String expectedUsername = "John.Doe1";

        // Simulate existing trainer with the same username
        Map<Long, Object> trainerStorage = new HashMap<>();
        Trainer trainer = new Trainer("John", "Doe", "John.Doe", "password", true, null);
        trainerStorage.put(1L, trainer);
        StorageImpl trainerStorageImpl = new StorageImpl();
        trainerStorageImpl.setMyStorage(trainerStorage);
        ((TrainerDaoImpl) trainerDao).setStorage(trainerStorageImpl);

        // Simulate empty trainee storage
        StorageImpl emptyStorage = new StorageImpl();
        emptyStorage.setMyStorage(new HashMap<>());
        ((TraineeDaoImpl) traineeDao).setStorage(emptyStorage);

        // Act
        String generatedUsername = userNameGenerator.generateUsername(firstName, lastName);

        // Assert
        assertEquals(expectedUsername, generatedUsername);
    }

    @Test
    public void testGenerateUsername_WithDuplicatesInBothTables() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String expectedUsername = "John.Doe2";

        // Simulate existing trainee and trainer with the same username
        Map<Long, Object> traineeStorage = new HashMap<>();
        Trainee trainee = new Trainee("John", "Doe", "John.Doe", "password", true, null, null);
        traineeStorage.put(1L, trainee);
        StorageImpl traineeStorageImpl = new StorageImpl();
        traineeStorageImpl.setMyStorage(traineeStorage);
        ((TraineeDaoImpl) traineeDao).setStorage(traineeStorageImpl);

        Map<Long, Object> trainerStorage = new HashMap<>();
        Trainer trainer = new Trainer("John", "Doe", "John.Doe", "password", true, null);
        trainerStorage.put(1L, trainer);
        StorageImpl trainerStorageImpl = new StorageImpl();
        trainerStorageImpl.setMyStorage(trainerStorage);
        ((TrainerDaoImpl) trainerDao).setStorage(trainerStorageImpl);

        // Act
        String generatedUsername = userNameGenerator.generateUsername(firstName, lastName);

        // Assert
        assertEquals(expectedUsername, generatedUsername);
    }

    @Test
    public void testGenerateUsername_WithSpecialCharactersInNames() {
        // Arrange
        String firstName = "Jo-hn";
        String lastName = "Doe_2";
        String expectedUsername = "Jo-hn.Doe_2";

        // Simulate empty storage
        StorageImpl emptyStorage = new StorageImpl();
        emptyStorage.setMyStorage(new HashMap<>());
        ((TraineeDaoImpl) traineeDao).setStorage(emptyStorage);
        ((TrainerDaoImpl) trainerDao).setStorage(emptyStorage);

        // Act
        String generatedUsername = userNameGenerator.generateUsername(firstName, lastName);

        // Assert
        assertEquals(expectedUsername, generatedUsername);
    }

    @Test
    public void testGenerateUsername_WithMixedCaseNames() {
        // Arrange
        String firstName = "jOHN";
        String lastName = "doE";
        String expectedUsername = "jOHN.doE";

        // Simulate empty storage
        StorageImpl emptyStorage = new StorageImpl();
        emptyStorage.setMyStorage(new HashMap<>());
        ((TraineeDaoImpl) traineeDao).setStorage(emptyStorage);
        ((TrainerDaoImpl) trainerDao).setStorage(emptyStorage);

        // Act
        String generatedUsername = userNameGenerator.generateUsername(firstName, lastName);

        // Assert
        assertEquals(expectedUsername, generatedUsername);
    }

}
