package com.uw.service;


import com.uw.dao.TrainerDaoImpl;
import com.uw.model.Trainer;
import com.uw.model.TrainingType;
import com.uw.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerServiceTest {

    SessionFactory sessionFactory;
    private final TrainerService trainerService = new TrainerServiceImpl();

    @AfterEach
    public void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    public void setUp() {
        sessionFactory = new MetadataSources(new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build())
                .buildMetadata()
                .buildSessionFactory();
        TrainerDaoImpl trainerDao = new TrainerDaoImpl();
        trainerDao.setSessionFactory(sessionFactory);
        ((TrainerServiceImpl) trainerService).setTrainerDao(trainerDao);
    }

   @Test
    public void testCreateTrainer() throws Exception {
        // Inicializar datos del entrenador
        String name = "John";
        String lastName = "Doe";
        String username = name + "." + lastName;
        String password = "password";
        String specialization = "Java";

        User user = new User(name, lastName, username, password, true);
        // Crear un objeto de entrenador
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        TrainingType trainingType = new TrainingType();
        trainingType.setTypeName(specialization);
        trainer.setSpecialization(trainingType);

        // Crear el entrenador en el servicio y obtener el ID
        long id = trainerService.createTrainer(trainer);

        // Verificar que el ID sea mayor que cero
        assertTrue(id > 0);

        // Verificar que el entrenador no sea nulo
        assertNotNull(trainer);

        // Verificar que el entrenador se pueda recuperar del servicio y sea igual al creado
        assertEquals(trainer.toString(), trainerService.selectTrainerProfile(id).toString());
    }

    @Test
    public void testUpdateTrainer() throws Exception {
        // Crear un entrenador inicial
        String name = "InitialName";
        String lastName = "InitialLastName";
        String username = name + "." + lastName;
        String password = "initialPassword";
        String specialization = "InitialSpecialization";

        User user = new User(name, lastName, username, password, true);
        Trainer initialTrainer = new Trainer();

        initialTrainer.setUser(user);
        TrainingType trainingType = new TrainingType();
        trainingType.setTypeName(specialization);
        initialTrainer.setSpecialization(trainingType);

        // Crear el entrenador en el servicio y obtener el ID
        long id = trainerService.createTrainer(initialTrainer);

        // Modificar algunos atributos del entrenador
        String updatedName = "UpdatedName";
        String updatedLastName = "UpdatedLastName";

        initialTrainer.getUser().setFirstName(updatedName);
        initialTrainer.getUser().setLastName(updatedLastName);

        // Llamar al método updateTrainer
        trainerService.updateTrainer(initialTrainer);

        // Verificar que el entrenador se haya actualizado correctamente
        Trainer updatedTrainer = trainerService.selectTrainerProfile(id);

        assertNotNull(updatedTrainer);
        assertEquals(updatedName, updatedTrainer.getUser().getFirstName());
        assertEquals(updatedLastName, updatedTrainer.getUser().getLastName());
    }

    @Test
    public void testSelectTrainerProfile_Success() throws Exception {
        // Crear un entrenador
        String name = "TestName";
        String lastName = "TestLastName";
        String username = name + "." + lastName;
        String password = "testPassword";
        String specialization = "TestSpecialization";
        User user = new User(name, lastName, username, password, true);
        Trainer trainer = new Trainer();

        trainer.setUser(user);
        TrainingType trainingType = new TrainingType();
        trainingType.setTypeName(specialization);
        trainer.setSpecialization(trainingType);

        // Crear el entrenador en el servicio y obtener el ID
        long id = trainerService.createTrainer(trainer);

        // Obtener el entrenador del servicio usando su ID
        Trainer selectedTrainer = trainerService.selectTrainerProfile(id);

        // Verificar que el entrenador seleccionado sea igual al entrenador creado
        assertEquals(trainer.toString(), selectedTrainer.toString());
    }

    @Test
    public void testSelectTrainerProfile_Failure() throws Exception {
        // Intentar seleccionar un entrenador con un ID inexistente
        long nonExistentId = 12345L;
        assertThrows(Exception.class, () ->{
            trainerService.selectTrainerProfile(nonExistentId);
        });
    }
}
