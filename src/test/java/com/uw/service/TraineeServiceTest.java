package com.uw.service;

import com.uw.dao.TraineeDaoImpl;
import com.uw.model.Trainee;
import com.uw.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

public class TraineeServiceTest {

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
        traineeDao.setSessionFactory(sessionFactory);
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

        User user = new User(name, lastName, username, password,true);

        //Creating trainee
        Trainee trainee = new Trainee(dateOfBirth, address);
        trainee.setUser(user);

        //Creating trainee in the service and getting the id
        long id = traineeService.createTrainee(trainee);

        assertTrue(id > 0);
        assertNotNull(trainee);
        assertNotNull(traineeService.selectTraineeProfile(id));
        assertEquals(traineeService.selectTraineeProfile(id).toString(), trainee.toString());

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

        User user = new User( name, lastName, username, password, true );
        Trainee initialTrainee = new Trainee( dateOfBirth, address );
        initialTrainee.setUser( user );
        // Crear el trainee en el servicio y obtener el ID
        long id = traineeService.createTrainee(initialTrainee);

        // Modificar algunos atributos del trainee
        String updatedName = "UpdatedName";
        String updatedLastName = "UpdatedLastName";
        String updatedAddress = "UpdatedAddress";

        initialTrainee.getUser().setFirstName(updatedName);
        initialTrainee.getUser().setLastName(updatedLastName);
        initialTrainee.setAddress(updatedAddress);

        // Llamar al método updateTrainee
        traineeService.updateTrainee(initialTrainee);

        // Verificar que el trainee se haya actualizado correctamente
        Trainee updatedTrainee = traineeService.selectTraineeProfile(id);

        assertNotNull(updatedTrainee);
        assertEquals(updatedName, updatedTrainee.getUser().getFirstName());
        assertEquals(updatedLastName, updatedTrainee.getUser().getLastName());
        assertEquals(updatedAddress, updatedTrainee.getAddress());
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

        //User name, lastName, username, password, true,
        Trainee trainee = new Trainee(dateOfBirth, address);

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
        User user = new User(name, lastName, username, password, true);
        Trainee trainee = new Trainee(dateOfBirth, address);
        trainee.setUser(user);
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
        assertEquals(trainee.toString(), selectedTrainee.toString());
    }

    // Test para selectTraineeProfile (caso fallido)
    @Test(expected = Exception.class)
    public void testSelectTraineeProfile_Failure() throws Exception {
        // Intentar seleccionar un trainee con un ID inexistente
        long nonExistentId = 12345L;
        traineeService.selectTraineeProfile(nonExistentId);
    }

}
