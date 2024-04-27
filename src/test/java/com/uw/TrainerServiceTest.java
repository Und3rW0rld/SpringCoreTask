package com.uw;

import com.uw.dao.TrainerDao;
import com.uw.dao.TrainerDaoImpl;
import com.uw.model.Trainer;
import com.uw.service.TrainerService;
import com.uw.service.TrainerServiceImpl;
import com.uw.util.Storage;
import com.uw.util.StorageImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class TrainerServiceTest {

    private final Storage storage = new StorageImpl();
    private final TrainerService trainerService = new TrainerServiceImpl();
    private final TrainerDao trainerDao = new TrainerDaoImpl();

    @Before
    public void setUp() {
        ((StorageImpl) storage).setMyStorage(new HashMap<>());
        ((TrainerDaoImpl) trainerDao).setStorage(storage);
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

        // Crear un objeto de entrenador
        Trainer trainer = new Trainer(name, lastName, username, password, true, specialization);

        // Crear el entrenador en el servicio y obtener el ID
        long id = trainerService.createTrainer(trainer);

        // Verificar que el ID sea mayor que cero
        assertTrue(id > 0);

        // Verificar que el entrenador no sea nulo
        assertNotNull(trainer);

        // Verificar que el entrenador se pueda recuperar del servicio y sea igual al creado
        assertEquals(trainer, trainerService.selectTrainerProfile(id));
    }

    @Test
    public void testUpdateTrainer() throws Exception {
        // Crear un entrenador inicial
        String name = "InitialName";
        String lastName = "InitialLastName";
        String username = name + "." + lastName;
        String password = "initialPassword";
        String specialization = "InitialSpecialization";

        Trainer initialTrainer = new Trainer(name, lastName, username, password, true, specialization);

        // Crear el entrenador en el servicio y obtener el ID
        long id = trainerService.createTrainer(initialTrainer);

        // Modificar algunos atributos del entrenador
        String updatedName = "UpdatedName";
        String updatedLastName = "UpdatedLastName";
        String updatedSpecialization = "UpdatedSpecialization";

        initialTrainer.setFirstName(updatedName);
        initialTrainer.setLastName(updatedLastName);
        initialTrainer.setSpecialization(updatedSpecialization);

        // Llamar al método updateTrainer
        trainerService.updateTrainer(initialTrainer);

        // Verificar que el entrenador se haya actualizado correctamente
        Trainer updatedTrainer = trainerService.selectTrainerProfile(id);

        assertNotNull(updatedTrainer);
        assertEquals(updatedName, updatedTrainer.getFirstName());
        assertEquals(updatedLastName, updatedTrainer.getLastName());
        assertEquals(updatedSpecialization, updatedTrainer.getSpecialization());
    }

    @Test
    public void testSelectTrainerProfile_Success() throws Exception {
        // Crear un entrenador
        String name = "TestName";
        String lastName = "TestLastName";
        String username = name + "." + lastName;
        String password = "testPassword";
        String specialization = "TestSpecialization";

        Trainer trainer = new Trainer(name, lastName, username, password, true, specialization);

        // Crear el entrenador en el servicio y obtener el ID
        long id = trainerService.createTrainer(trainer);

        // Obtener el entrenador del servicio usando su ID
        Trainer selectedTrainer = trainerService.selectTrainerProfile(id);

        // Verificar que el entrenador seleccionado sea igual al entrenador creado
        assertEquals(trainer, selectedTrainer);
    }

    @Test(expected = Exception.class)
    public void testSelectTrainerProfile_Failure() throws Exception {
        // Intentar seleccionar un entrenador con un ID inexistente
        long nonExistentId = 12345L;
        trainerService.selectTrainerProfile(nonExistentId);
    }
}
