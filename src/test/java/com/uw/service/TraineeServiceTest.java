package com.uw.service;

import com.uw.dao.TraineeDao;
import com.uw.dao.TraineeDaoImpl;
import com.uw.model.Trainee;
import com.uw.util.Storage;
import com.uw.util.StorageImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;

public class TraineeServiceTest {

    Storage storage = new StorageImpl();
    TraineeService traineeService = new TraineeServiceImpl();
    TraineeDao traineeDao = new TraineeDaoImpl();

    @Before
    public void setUp(){
        ((StorageImpl) storage).setMyStorage(new HashMap<>());
        ((TraineeDaoImpl) traineeDao).setStorage(storage);
        ((TraineeServiceImpl) traineeService).setTraineeDao(traineeDao);
    }

    @Test
    public void testCreateTrainee() throws Exception {
        //Initialize Data
        String name = "theName";
        String lastName = "theLastName";
        String username = name+"."+lastName;
        String password = "0123456789";
        LocalDate dateOfBirth = LocalDate.of(2000, 2, 12);
        String address = "myAddress";

        //Creating trainee
        Trainee trainee = new Trainee(name, lastName, username, password,true, dateOfBirth, address);
        //Creating trainee in the service and getting the id
        long id = traineeService.createTrainee(trainee);

        assertTrue(id > 0);
        assertNotNull(trainee);
        assertNotNull(traineeService.selectTraineeProfile(id));
        assertEquals(traineeService.selectTraineeProfile(id), trainee);

    }
    @Test
    public void testUpdateTrainee() throws Exception {
        // Crear un trainee inicial
        String name = "InitialName";
        String lastName = "InitialLastName";
        String username = name + "." + lastName;
        String password = "initialPassword";
        LocalDate dateOfBirth = LocalDate.of(2000, 1, 1);
        String address = "InitialAddress";

        Trainee initialTrainee = new Trainee(name, lastName, username, password, true, dateOfBirth, address);

        // Crear el trainee en el servicio y obtener el ID
        long id = traineeService.createTrainee(initialTrainee);

        // Modificar algunos atributos del trainee
        String updatedName = "UpdatedName";
        String updatedLastName = "UpdatedLastName";
        String updatedAddress = "UpdatedAddress";

        initialTrainee.setFirstName(updatedName);
        initialTrainee.setLastName(updatedLastName);
        initialTrainee.setAdress(updatedAddress);

        // Llamar al método updateTrainee
        traineeService.updateTrainee(initialTrainee);

        // Verificar que el trainee se haya actualizado correctamente
        Trainee updatedTrainee = traineeService.selectTraineeProfile(id);

        assertNotNull(updatedTrainee);
        assertEquals(updatedName, updatedTrainee.getFirstName());
        assertEquals(updatedLastName, updatedTrainee.getLastName());
        assertEquals(updatedAddress, updatedTrainee.getAdress());
    }

    // Test para deleteTrainee
    @Test
    public void testDeleteTrainee() throws Exception {
        // Crear un trainee
        String name = "TestName";
        String lastName = "TestLastName";
        String username = name + "." + lastName;
        String password = "testPassword";
        LocalDate dateOfBirth = LocalDate.of(1990, 5, 15);
        String address = "TestAddress";

        Trainee trainee = new Trainee(name, lastName, username, password, true, dateOfBirth, address);

        // Crear el trainee en el servicio y obtener el ID
        long id = traineeService.createTrainee(trainee);

        // Verificar que el trainee exista antes de eliminarlo
        assertNotNull(traineeService.selectTraineeProfile(id));

        // Eliminar el trainee
        try {
            traineeService.deleteTrainee(id);
        } catch (Exception e) {
            fail("Error al eliminar el trainee: " + e.getMessage());
        }

        // Verificar que el trainee ya no exista después de eliminarlo
        try {
            traineeService.selectTraineeProfile(id);
            fail("Se esperaba una excepción ya que el trainee debería haber sido eliminado");
        } catch (Exception ignored) {
            // Se espera una excepción ya que el trainee debería haber sido eliminado
            System.out.println("Se lanzó la excepción");
        }

        try{
            traineeService.deleteTrainee(id);
            fail("Se espera una excepción ya que este id ya no existe");
        }catch (Exception ignored) {
            System.out.println("Se lanzó la excepción");
        }

    }

    // Test para selectTraineeProfile (caso exitoso)
    @Test
    public void testSelectTraineeProfile_Success() {
        // Crear un trainee
        String name = "TestName";
        String lastName = "TestLastName";
        String username = name + "." + lastName;
        String password = "testPassword";
        LocalDate dateOfBirth = LocalDate.of(1990, 5, 15);
        String address = "TestAddress";

        Trainee trainee = new Trainee(name, lastName, username, password, true, dateOfBirth, address);

        // Crear el trainee en el servicio y obtener el ID
        long id = traineeService.createTrainee(trainee);

        // Obtener el trainee del servicio usando su ID
        Trainee selectedTrainee;
        try {
            selectedTrainee = traineeService.selectTraineeProfile(id);
        } catch (Exception e) {
            fail("Error al seleccionar el trainee: " + e.getMessage());
            return;
        }

        // Verificar que el trainee seleccionado sea igual al trainee creado
        assertEquals(trainee, selectedTrainee);
    }

    // Test para selectTraineeProfile (caso fallido)
    @Test(expected = Exception.class)
    public void testSelectTraineeProfile_Failure() throws Exception {
        // Intentar seleccionar un trainee con un ID inexistente
        long nonExistentId = 12345L;
        traineeService.selectTraineeProfile(nonExistentId);
    }


}
