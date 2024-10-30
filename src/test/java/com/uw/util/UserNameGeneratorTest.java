package com.uw.util;

import com.uw.dao.TraineeDaoImpl;
import com.uw.dao.TrainerDaoImpl;
import com.uw.dao.UserDaoImpl;
import com.uw.model.Trainee;
import com.uw.model.Trainer;
import com.uw.model.User;
import com.uw.service.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.HashMap;
import java.util.Map;

/*
public class UserNameGeneratorTest {

   private UserNameGenerator userNameGenerator;
   private final UserService userService = new UserService();
   private final TrainerService trainerService = new TrainerServiceImpl();
    SessionFactory sessionFactory;
    private final TraineeService traineeService = new TraineeServiceImpl();

    @After
    public void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Before
    public void setUp() {
        sessionFactory = new MetadataSources(new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build())
                .buildMetadata()
                .buildSessionFactory();
        TraineeDaoImpl traineeDao = new TraineeDaoImpl();
        TrainerDaoImpl trainerDao = new TrainerDaoImpl();
        traineeDao.setSessionFactory(sessionFactory);
        trainerDao.setSessionFactory(sessionFactory);
        ((TraineeServiceImpl) traineeService).setTraineeDao(traineeDao);
        ((TrainerServiceImpl) trainerService).setTrainerDao(trainerDao);
        userNameGenerator = new UserNameGenerator();

        UserDaoImpl userDao = new UserDaoImpl();
        userDao.setSessionFactory(sessionFactory);
        ((UserServiceImpl) userService).setUserDao(userDao);
        userNameGenerator = new UserNameGenerator();

        userNameGenerator.setUserServiceImpl(userService);
    }

    @Test
    public void testGenerateUsername_NoDuplicates() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String expectedUsername = "John.Doe";
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
        User user = new User("John", "Doe", "John.Doe", "password", true);
        Trainee trainee = new Trainee(null, null);
        trainee.setUser(user);

        traineeService.createTrainee(trainee);

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

        User user = new User("John", "Doe", "John.Doe", "password", true);
        Trainee trainee = new Trainee( null, null);

        trainee.setUser(user);

        traineeService.createTrainee(trainee);
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

        User user = new User("John", "Doe", "John.Doe", "password", true);
        Trainer trainer = new Trainer();
        trainer.setSpecialization(null);
        trainer.setUser(user);
        trainerService.createTrainer(trainer);

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

        User user = new User("John", "Doe", "John.Doe", "password", true);
        Trainee trainee = new Trainee(null, null);
        trainee.setUser(user);
        traineeService.createTrainee(trainee);

        user = new User("John", "Doe", "John.Doe", "password", true);
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(null);
        trainerService.createTrainer(trainer);

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

        // Act
        String generatedUsername = userNameGenerator.generateUsername(firstName, lastName);

        // Assert
        assertEquals(expectedUsername, generatedUsername);
    }
}
*/