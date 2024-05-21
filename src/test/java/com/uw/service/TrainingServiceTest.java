package com.uw.service;

import com.uw.dao.TrainingDao;
import com.uw.dao.TrainingDaoImpl;
import com.uw.model.Training;
import com.uw.model.TrainingType;
import com.uw.util.Storage;
import com.uw.util.StorageImpl;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.Assert.*;

public class TrainingServiceTest {

    private Storage storage = new StorageImpl();
    private TrainingService trainingService = new TrainingServiceImpl();
    private TrainingDao trainingDao = new TrainingDaoImpl();

    @Before
    public void setUp() {
        ((StorageImpl) storage).setMyStorage(new HashMap<>());
        ((TrainingDaoImpl) trainingDao).setStorage(storage);
        ((TrainingServiceImpl) trainingService).setTrainingDao(trainingDao);
    }

    @Test
    public void testCreateTraining() throws Exception {
        // Inicializar datos de la capacitación
        long traineeId = 1L;
        long trainerId = 1L;
        String trainingName = "Java Basics";
        TrainingType trainingType = new TrainingType("Workshop");
        LocalDate trainingDate = LocalDate.of(2024, 5, 1);
        String trainingDuration = "2 hours";

        // Crear un objeto de capacitación
        Training training = new Training(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);

        // Crear la capacitación en el servicio y obtener el ID
        long id = trainingService.createTraining(training);

        // Verificar que el ID sea mayor que cero
        assertTrue(id > 0);

        // Verificar que la capacitación no sea nula
        assertNotNull(training);

        // Verificar que la capacitación se pueda recuperar del servicio y sea igual a la creada
        assertEquals(training, trainingService.selectTrainingProfile(id));
    }

    @Test
    public void testSelectTrainingProfile_Success() throws Exception {
        // Crear una capacitación
        long traineeId = 1L;
        long trainerId = 1L;
        String trainingName = "Java Basics";
        TrainingType trainingType = new TrainingType("Workshop");
        LocalDate trainingDate = LocalDate.of(2024, 5, 1);
        String trainingDuration = "2 hours";

        Training training = new Training(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);

        // Crear la capacitación en el servicio y obtener el ID
        long id = trainingService.createTraining(training);

        // Obtener la capacitación del servicio usando su ID
        Training selectedTraining = trainingService.selectTrainingProfile(id);

        // Verificar que la capacitación seleccionada sea igual a la capacitación creada
        assertEquals(training, selectedTraining);
    }

    @Test(expected = Exception.class)
    public void testSelectTrainingProfile_Failure() throws Exception {
        // Intentar seleccionar una capacitación con un ID inexistente
        long nonExistentId = 12345L;
        trainingService.selectTrainingProfile(nonExistentId);
    }
}
