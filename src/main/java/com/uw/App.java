package com.uw;

import com.uw.facade.Facade;
import com.uw.model.Trainee;
import com.uw.model.Trainer;
import com.uw.model.Training;
import com.uw.model.TrainingType;
import com.uw.service.TraineeService;
import com.uw.service.TrainerService;
import com.uw.service.TrainingService;
import com.uw.util.PasswordGeneratorImpl;
import com.uw.util.UserNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.logging.Logger;

public class App {

    // Colores ANSI
    static final String RESET = "\u001B[0m";
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String BLUE = "\u001B[34m";
    static final String PURPLE = "\u001B[35m";
    static final String CYAN = "\u001B[36m";
    static final String WHITE = "\u001B[37m";
    static ApplicationContext applicationContext;
    //Scanner for input use.
    static Scanner sc = new Scanner(System.in);

    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {

        // Inicialización del contexto y del facade
        applicationContext = new AnnotationConfigApplicationContext(com.uw.config.AppConfig.class);
        Facade facade = applicationContext.getBean(Facade.class);

        // Resto de la lógica de la aplicación...
        mostrarBanner();
        System.out.println(GREEN+"Welcome to the GYM CRM System! \uD83D\uDCAA"+RESET +CYAN+
                                 "\n=============================="+RESET);

        while(true) {
            System.out.println(PURPLE+"\nWhat do you want to do?"+RESET);
            displayMenu();
            System.out.print("Type a number: ");
            String line = sc.nextLine();
            if (line.equals("1")){
                //Trainee options
                traineeMenu(facade.getTraineeService());
            } else if(line.equals("2")){
                //Trainer options
                trainerMenu(facade.getTrainerService());
            }else if(line.equals("3")){
                //Training options
                trainingMenu(facade.getTrainingService());
            }else if (line.equals("4")){
                break;
            }else{
                System.out.println("That is not a valid option \uD83E\uDD28 \nTry again!");
            }
        }
        System.out.println(YELLOW+"See you! \uD83D\uDE38"+RESET);

    }

    private static void trainingMenu(TrainingService trainingService) {
        while ( true ){
            System.out.println("\nTraining options:"+
                    "\n1. Create Training"+
                    "\n2. Select Training Profile"+
                    "\n3. Back"
            );
            System.out.print("Type a number: ");
            String line = sc.nextLine();
            if(line.equals("1")) {
                System.out.println("Create a new Training");
                System.out.print("Type the trainee id: ");
                long traineeId = Long.parseLong(sc.nextLine());
                System.out.print("Type the trainer id: ");
                long traineerId = Long.parseLong(sc.nextLine());
                System.out.print("Type the training name: ");
                String trainingName = sc.nextLine();
                System.out.print("Type the training type: ");
                String trainingTypeStr = sc.nextLine();
                TrainingType trainingType = new TrainingType(trainingTypeStr);
                System.out.print("Type the year of the training: ");
                String year = sc.nextLine();
                System.out.print("Type the month of training: ");
                String month = sc.nextLine();
                System.out.print("Type the day of the training: ");
                String day = sc.nextLine();
                LocalDate trainingDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                System.out.print("Type the training duration");
                String duration = sc.nextLine();
                Training training = new Training(traineeId, traineerId, trainingName,trainingType,trainingDate, duration);
                System.out.println(CYAN+"Trainer created successfully!"+RESET);
                System.out.println("This is the generated training: "+training);
                trainingService.createTraining(training);
                System.out.println("The training id is: "+training.getId());
            }else if( line.equals("2") ){
                System.out.println("Select a Training");
                System.out.print("Enter a training id: ");
                Training training;
                do{
                    long id = Long.parseLong(sc.nextLine());
                    try {
                        training = trainingService.selectTrainingProfile(id);
                        break;
                    } catch (Exception e) {
                        logger.severe("The id of the training was not found");
                        System.out.print("Try again: ");
                    }} while (true);
                System.out.println(training.toString());
            } else if (line.equals("3")) {
                return;
            }else{
                System.out.println("That is not a valid option \uD83E\uDD28 \nTry again!");
            }
        }
    }

    private static void trainerMenu(TrainerService trainerService) {
        while ( true ){
            System.out.println("\nTrainer options:"+
                    "\n1. Create Trainee"+
                    "\n2. Update Trainee"+
                    "\n3. Select Trainee Profile"+
                    "\n4. Back"
            );
            System.out.print("Type a number: ");
            String line = sc.nextLine();
            if(line.equals("1")) {
                System.out.println("Create a new Trainer");
                System.out.print("Type your name: ");
                String name = sc.nextLine();
                System.out.print("Type your lastname: ");
                String lastname = sc.nextLine();
                String username = applicationContext.getBean(UserNameGenerator.class).generateUsername(name, lastname);
                String password = new PasswordGeneratorImpl().generatePasword(10);
                System.out.print("Enter your Specialization: ");
                String specialization = sc.nextLine();
                Trainer trainer = new Trainer(name, lastname, username,password,true, specialization);
                System.out.println(CYAN+"Trainer created successfully!"+RESET);
                System.out.println("This is your generated username: "+username);
                System.out.println("And this is your generated password: "+password);
                trainerService.createTrainer(trainer);
                System.out.println("Your id is: "+trainer.getUserId());
            }else if(line.equals("2")) {
                System.out.println("Update trainer");
                System.out.print("Type the id of the trainer: ");
                Trainer trainer;
                do {
                    long id = Long.parseLong(sc.nextLine());
                    try {
                        trainer = trainerService.selectTrainerProfile(id);
                        break;
                    } catch (Exception e) {
                        logger.warning("That id is not registered.");
                        System.out.print( "Try again :");
                    }
                }while ( true );
                while(true) {
                    System.out.println("Trainer: " + trainer);
                    System.out.println("What do you want to update?" +
                            "\n1. Update name" +
                            "\n2. Update lastname" +
                            "\n3. Update status" +
                            "\n4. Specialization"+
                            "\n5. Back");
                    System.out.print("Type the option number: ");
                    String response = sc.nextLine();
                    if (response.equals("1")) {
                        System.out.print("Type the new name: ");
                        String newName = sc.nextLine();
                        trainer.setFirstName(newName);
                    } else if (response.equals("2")) {
                        System.out.print("Type the new lastname: ");
                        String newLastname = sc.nextLine();
                        trainer.setLastName(newLastname);
                    } else if (response.equals("3")) {
                        System.out.print("Type the new status (true or false): ");
                        String status = sc.nextLine();
                        boolean newStatus = Boolean.parseBoolean(status);
                        trainer.setActive(newStatus);
                    } else if (response.equals("4")) {
                        System.out.print("Type your specialization: ");
                        String specialization = sc.nextLine();
                        trainer.setSpecialization(specialization);
                    } else if (response.equals("5")){
                        break;
                    }else {
                        System.out.println("That is not a valid option \uD83E\uDD28 \nTry again!");
                    }
                    if(response.equals("1") || response.equals("2")){
                        String newUsername = applicationContext.getBean(UserNameGenerator.class).generateUsername(trainer.getFirstName(), trainer.getLastName());
                        if (newUsername.matches(trainer.getUsername()+"\\d*")){
                            newUsername = trainer.getUsername();
                        }
                        trainer.setUsername(newUsername);
                    }
                }
            }else if( line.equals("3") ){
                System.out.println("Select a Trainer");
                System.out.print("Enter a trainer id: ");
                Trainer trainer;
                do{
                    long id = Long.parseLong(sc.nextLine());
                    try {
                        trainer = trainerService.selectTrainerProfile(id);
                        break;
                    } catch (Exception e) {
                        logger.severe("The id of the trainer was not found");
                        System.out.print("Try again: ");
                    }} while (true);
                System.out.println(trainer.toString());
            } else if (line.equals("4")) {
                return;
            }else{
                System.out.println("That is not a valid option \uD83E\uDD28 \nTry again!");
            }
        }
    }

    private static void traineeMenu(TraineeService traineeService) {
        while ( true ){
            System.out.println("\nTrainee options:"+
                "\n1. Create Trainee"+
                "\n2. Update Trainee"+
                "\n3. Delete Trainee"+
                "\n4. Select Trainee Profile"+
                "\n5. Back"
            );
            System.out.print("Type a number: ");
            String line = sc.nextLine();
            if(line.equals("1")) {
                System.out.println("Create a new Trainee");
                System.out.print("Type your name: ");
                String name = sc.nextLine();
                System.out.print("Type your lastname: ");
                String lastname = sc.nextLine();
                String username = applicationContext.getBean(UserNameGenerator.class).generateUsername(name, lastname);
                String password = new PasswordGeneratorImpl().generatePasword(10);
                System.out.print("Type your year of birth: ");
                String year = sc.nextLine();
                System.out.print("Type your month of birth: ");
                String month = sc.nextLine();
                System.out.print("Type your day of birth: ");
                String day = sc.nextLine();
                LocalDate dateOfBirth = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                System.out.print("Enter your Address: ");
                String address = sc.nextLine();
                Trainee trainee = new Trainee(name, lastname, username,password,true,dateOfBirth,address);
                System.out.println(CYAN+"Trainee created successfully!"+RESET);
                System.out.println("This is your generated username: "+username);
                System.out.println("And this is your generated password: "+password);
                traineeService.createTrainee(trainee);
                System.out.println("Your id is: "+trainee.getUserId());
            }else if(line.equals("2")) {
                System.out.println("Update trainee");
                System.out.print("Type the id of the trainee: ");
                Trainee trainee;
                do {
                    long id = Long.parseLong(sc.nextLine());
                    try {
                        trainee = traineeService.selectTraineeProfile(id);
                        break;
                    } catch (Exception e) {
                        logger.warning("That id is not registered.");
                        System.out.print( "Try again :");
                    }
                }while ( true );
                while(true) {
                    System.out.println("Trainee: " + trainee);
                    System.out.println("What do you want to update?" +
                            "\n1. Update name" +
                            "\n2. Update lastname" +
                            "\n3. Update status" +
                            "\n4. Update date of birth" +
                            "\n5. Update address" +
                            "\n6. Back");
                    System.out.print("Type the option number: ");
                    String response = sc.nextLine();
                    if (response.equals("1")) {
                        System.out.print("Type the new name: ");
                        String newName = sc.nextLine();
                        trainee.setFirstName(newName);
                    } else if (response.equals("2")) {
                        System.out.print("Type the new lastname: ");
                        String newLastname = sc.nextLine();
                        trainee.setLastName(newLastname);
                    } else if (response.equals("3")) {
                        System.out.print("Type the new status (true or false): ");
                        String status = sc.nextLine();
                        boolean newStatus = Boolean.parseBoolean(status);
                        trainee.setActive(newStatus);
                    } else if (response.equals("4")) {
                        System.out.print("Type your year of birth: ");
                        String year = sc.nextLine();
                        System.out.print("Type your month of birth: ");
                        String month = sc.nextLine();
                        System.out.print("Type your day of birth: ");
                        String day = sc.nextLine();
                        LocalDate dateOfBirth = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                        trainee.setDateOfBirth(dateOfBirth);
                    } else if (response.equals("5")){
                        System.out.print("Type the new address: ");
                        String newAddress = sc.nextLine();
                        trainee.setAdress(newAddress);
                    } else if (response.equals("6")){
                        break;
                    }else {
                        System.out.println("That is not a valid option \uD83E\uDD28 \nTry again!");
                    }
                    if(response.equals("1") || response.equals("2")){
                        String newUsername = applicationContext.getBean(UserNameGenerator.class).generateUsername(trainee.getFirstName(), trainee.getLastName());
                        if (newUsername.matches(trainee.getUsername()+"\\d*")){
                            newUsername = trainee.getUsername();
                        }
                        trainee.setUsername(newUsername);
                    }
                }
            }else if( line.equals("3") ){
                System.out.println("Select a Trainee");
                System.out.print("Enter a trainee id: ");
                do{
                long id = Long.parseLong(sc.nextLine());
                try {
                    traineeService.deleteTrainee(id);
                    break;
                } catch (Exception e) {
                    logger.severe("The id of the trainee was not found");
                    System.out.print("Try again: ");
                }} while (true);

                System.out.println("User delete");
            }else if( line.equals("4") ) {
                System.out.println("Select a Trainee");
                System.out.print("Enter a trainee id: ");
                Trainee trainee;
                do{
                    long id = Long.parseLong(sc.nextLine());
                    try {
                        trainee = traineeService.selectTraineeProfile(id);
                        break;
                    } catch (Exception e) {
                        logger.severe("The id of the trainee was not found");
                        System.out.print("Try again: ");
                    }} while (true);
                System.out.println(trainee.toString());
            } else if (line.equals("5")) {
                    return;
            }else{
                 System.out.println("That is not a valid option \uD83E\uDD28 \nTry again!");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\nPress the option number depends on what do you wants to do:"+
                "\n1. Trainee options"+
                "\n2. Trainer options"+
                "\n3. Training options"+ RED+
                        "\n4. Exit"+RESET
                );
    }

    private static void mostrarBanner() {
        // Cargar el banner personalizado desde el archivo
        try (InputStream inputStream = App.class.getResourceAsStream("/banner.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            // Mostrar el contenido del banner en la consola
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(BLUE + line + RESET);
            }

        } catch (IOException e) {
            // Manejar cualquier excepción al cargar o mostrar el banner
            e.printStackTrace();
        }
    }



}
