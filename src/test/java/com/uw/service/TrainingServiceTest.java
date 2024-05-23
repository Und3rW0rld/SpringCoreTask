package com.uw.service;

import com.uw.dao.TrainingDaoImpl;
import com.uw.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class TrainingServiceTest {

    SessionFactory sessionFactory;
    private final TrainingService trainingService = new TrainingServiceImpl();

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
        TrainingDaoImpl trainingDao = new TrainingDaoImpl();
        trainingDao.setSessionFactory(sessionFactory);
        ((TrainingServiceImpl) trainingService).setTrainingDao(trainingDao);
    }

    private static Training getTraining(LocalDate dateOfBirth) {
        Trainee trainee = new Trainee(dateOfBirth, "some address");
        String name = "theName";
        String lastName = "theLastName";
        String username = name+"."+lastName;
        String password = "0123456789";
        User user = new User(name, lastName, username, password, true);
        trainee.setUser(user);
        long trainerId = 1L;

        Trainer trainer = new Trainer();
        TrainingType trainingType = new TrainingType();
        trainingType.setTypeName("YOGA");
        trainer.setSpecialization(trainingType);
        trainer.setUser( user );

        String trainingName = "Java Basics";
        trainingType = new TrainingType();
        trainingType.setTypeName("Workshop");
        LocalDate trainingDate = LocalDate.of(2024, 5, 1);
        int trainingDuration = 120;

        // Crear un objeto de capacitación
        Training training = new Training(trainingName, trainingType, trainingDate, trainingDuration);

        training.setTrainee(trainee);
        training.setTrainer(trainer);
        return training;
    }

    @Test
    public void testCreateTraining() throws Exception {
        // Inicializar datos de la capacitación
        LocalDate dateOfBirth = LocalDate.of(2000, 2, 12);
        Training training = getTraining(dateOfBirth);


        // Crear la capacitación en el servicio y obtener el ID
        long id = trainingService.createTraining(training);

        // Verificar que el ID sea mayor que cero
        assertTrue(id > 0);

        // Verificar que la capacitación no sea nula
        assertNotNull(training);

        // Verificar que la capacitación se pueda recuperar del servicio y sea igual a la creada
        assertEquals(training.toString(), trainingService.selectTrainingProfile(id).toString());
    }



    @Test
    public void testSelectTrainingProfile_Success() throws Exception {
        // Crear una capacitación
        String trainingName = "Java Basics";
        TrainingType trainingType = new TrainingType();
        trainingType.setTypeName("Workshop");
        LocalDate trainingDate = LocalDate.of(2024, 5, 1);
        String trainingDuration = "2 hours";

        LocalDate dateOfBirth = LocalDate.of(2000, 2, 12);
        Training training = getTraining(dateOfBirth);

        // Crear la capacitación en el servicio y obtener el ID
        long id = trainingService.createTraining(training);

        // Obtener la capacitación del servicio usando su ID
        Training selectedTraining = trainingService.selectTrainingProfile(id);

        // Verificar que la capacitación seleccionada sea igual a la capacitación creada
        assertEquals(training.toString(), selectedTraining.toString());
    }

    @Test(expected = Exception.class)
    public void testSelectTrainingProfile_Failure() throws Exception {
        // Intentar seleccionar una capacitación con un ID inexistente
        long nonExistentId = 12345L;
        trainingService.selectTrainingProfile(nonExistentId);
    }
}
