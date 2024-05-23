package com.uw;

import com.uw.facade.Facade;
import com.uw.model.*;
import com.uw.service.TraineeService;
import com.uw.service.TrainerService;
import com.uw.service.TrainingService;
import com.uw.service.UserService;
import com.uw.util.PasswordGeneratorImpl;
import com.uw.util.UserNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
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

    static Facade facade;

    private static final Logger logger = Logger.getLogger(App.class.getName());

    static {
        // Configuración del logger
        logger.setLevel(Level.ALL);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
    }

    public static void main(String[] args) {

        // Inicialización del contexto y del facade
        applicationContext = new AnnotationConfigApplicationContext(com.uw.config.AppConfig.class);
        facade = applicationContext.getBean(Facade.class);

        // Resto de la lógica de la aplicación...
        mostrarBanner();
        System.out.println(GREEN + "Welcome to the GYM CRM System! \uD83D\uDCAA" + RESET + CYAN +
                "\n==============================");

        label:
        while (true) {
            System.out.println(PURPLE + "\nWhat do you want to do?" + RESET);
            displayMenu();
            System.out.print("Type a number: ");
            String line = sc.nextLine();
            switch (line) {
                case "1":
                    //Create a new Trainee
                    createTrainee(facade.getTraineeService());
                    break;
                case "2":
                    //Trainer options
                    createTrainer(facade.getTrainerService());
                    break;
                case "3":
                    loginUser(facade.getUserService(), facade.getTraineeService(), facade.getTrainerService(), facade.getTrainingService());
                    break;
                case "4":
                    break label;
                default:
                    nonDefineOption();
                    break;
            }
        }
        System.out.println(YELLOW + "See you! \uD83D\uDE38" + RESET);

    }

    private static void loginUser(UserService userService, TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        logger.info("Login initiated");
        System.out.println("WELCOME :D");
        System.out.print("Enter your username: ");
        String username = sc.nextLine();
        while (!username.equalsIgnoreCase("break") && !userService.validateUsername(username)) {
            logger.warning("Invalid username: " + username);
            System.out.println("The username was not found. Try again (if you wanna return type: 'break'): ");
            username = sc.nextLine();
        }

        if (username.equalsIgnoreCase("break")) {
            logger.info("Login cancelled");
            return;
        }

        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        while (!password.equalsIgnoreCase("break") && !userService.validatePassword(username, password)) {
            logger.warning("Invalid password for username: " + username);
            System.out.println("The password is wrong. Try again (if you wanna return type: 'break'): ");
            password = sc.nextLine();
        }

        if (password.equalsIgnoreCase("break")) {
            logger.info("Login cancelled");
            return;
        }
        User user = userService.findUserByUsername(username);
        if (traineeService.existTraineeByUserId(user.getId()) != null) {
            //Verifies if the user is A trainee or a trainer
            traineeMenu(user, traineeService, userService, trainingService);
        } else {
            trainerMenu(user, trainerService, userService);
        }
    }

    private static void trainerMenu(User user, TrainerService trainerService, UserService userService) {
        System.out.println("TRAINER MENU");
        System.out.println(CYAN+"Welcome @"+user.getUsername()+"!"+RESET);
        Trainer trainer = trainerService.getTrainerByUser(user);
        label:
        while(true) {
            System.out.println(PURPLE+"\nWhat do you want to do?"+RESET);
            trainerOptions();
            System.out.print("Type a number: ");
            String line = sc.nextLine();
            switch (line) {
                case "1":
                    //Change your password
                    chageUserPassword(user, userService);
                    trainer.setUser(user);
                    trainerService.updateTrainer(trainer);
                    break;
                case "2":
                    //Update profile
                    updateTrainerProfile(trainer, trainerService);
                    break;
                case "3":
                    //Active de-active profile
                    boolean value = trainer.getUser().isActive();
                    value = !value;
                    trainer.getUser().setActive(value);
                    if(!value){
                        System.out.println("You profile was desactive");
                    }else{
                        System.out.println("Your profile was active");
                    }
                    trainerService.updateTrainer(trainer);
                    break;
                case "4":
                    // Get Training List
                    System.out.print("Enter start date (yyyy-mm-dd) or leave empty: ");
                    String startDateStr = sc.nextLine();
                    LocalDate startDate = startDateStr.isEmpty() ? null : LocalDate.parse(startDateStr);

                    System.out.print("Enter end date (yyyy-mm-dd) or leave empty: ");
                    String endDateStr = sc.nextLine();
                    LocalDate endDate = endDateStr.isEmpty() ? null : LocalDate.parse(endDateStr);

                    System.out.print("Enter trainee name or leave empty: ");
                    String traineeName = sc.nextLine();
                    traineeName = traineeName.isEmpty() ? null : traineeName;

                    List<Training> trainings = facade.getTrainingService().getTrainerTrainings(
                            trainer.getUser().getUsername(),
                            startDate,
                            endDate,
                            traineeName
                    );

                    System.out.println("Trainings List:");
                    for (Training training : trainings) {
                        System.out.println("Date: " + training.getTrainingDate() +
                                ", Trainee: " + training.getTrainee().getUser().getUsername() +
                                ", Type: " + training.getTrainingType().getTypeName());
                    }

                    break;
                case "5":
                    //Add Training
                    System.out.print("Enter trainee ID: ");
                    long traineeId = Long.parseLong(sc.nextLine());

                    Trainee trainee = null;
                    try {
                        trainee = facade.getTraineeService().selectTraineeProfile(traineeId);
                    } catch (Exception e) {
                        logger.severe("That id was not found");
                        break;
                    }
                    if (trainee == null) {
                        System.out.println("Trainee not found.");
                        break;
                    }

                    System.out.println("Enter the training name: ");
                    String trainingName = sc.nextLine();

                    System.out.print("Enter training date (yyyy-mm-dd): ");
                    LocalDate trainingDate = LocalDate.parse(sc.nextLine());

                    System.out.println("Enter the training type: ");
                    String trainingTypeStr = sc.nextLine();

                    TrainingType trainingType = new TrainingType();

                    trainingType.setTypeName(trainingTypeStr);

                    System.out.println("Enter the training duration (minutes): ");
                    int trainingDuration = Integer.parseInt(sc.nextLine());

                    Training training = new Training(trainingName, trainingType, trainingDate, trainingDuration);

                    training.setTrainer(trainer);
                    training.setTrainee(trainee);

                    facade.getTrainingService().createTraining(training);

                    System.out.println(training);

                    System.out.println("Training added successfully.");
                    break;
                case "6":
                    break label;
                default:
                    nonDefineOption();
                    break;
            }
        }
    }
    private static void updateTrainerProfile(Trainer trainer, TrainerService trainerService) {
        System.out.println("Update your profile");
        while(true) {
            System.out.println("What do you want to update?" +
                    "\n1. Update name" +
                    "\n2. Update lastname" +
                    "\n3. Update Specialization" +
                    "\n4. Back");
            System.out.print("Type the option number: ");
            String response = sc.nextLine();
            if (response.equals("1")) {
                System.out.print("Type the new name: ");
                String newName = sc.nextLine();
                trainer.getUser().setFirstName(newName);
            } else if (response.equals("2")) {
                System.out.print("Type the new lastname: ");
                String newLastname = sc.nextLine();
                trainer.getUser().setLastName(newLastname);
            } else if (response.equals("3")) {
                System.out.print("Type your specialization: ");
                String specialization = sc.nextLine();
                TrainingType trainingType = new TrainingType();
                trainingType.setTypeName(specialization);
                trainer.setSpecialization(trainingType);
            } else if (response.equals("4")){
                break;
            }else {
                System.out.println("That is not a valid option \uD83E\uDD28 \nTry again!");
            }
            if(response.equals("1") || response.equals("2")){
                String newUsername = applicationContext.getBean(UserNameGenerator.class).generateUsername(trainer.getUser().getFirstName(), trainer.getUser().getLastName());
                if (newUsername.matches(trainer.getUser().getUsername()+"\\d*")){
                    newUsername = trainer.getUser().getUsername();
                }
                trainer.getUser().setUsername(newUsername);
            }
        }
        trainerService.updateTrainer(trainer);
    }

    private static void trainerOptions() {
        System.out.println("\nPress the option number depends on what do you wants to do:"+
                "\n1. Change your password"+
                "\n2. Update your Profile"+
                "\n3. Activate/De-activate your profile "+
                "\n4. Get Trainings list "+
                "\n5. Add Training"+ RED +
                "\n6. Back"+RESET
        );
    }


    private static void chageUserPassword(User user, UserService userService) {
        System.out.println("Change your password");
        System.out.print("Type your new password: ");
        String password = sc.nextLine();
        user.setPassword(password);
        if(userService.updateUser(user)){
            System.out.println("Password change successfully");
        }else{
            System.out.println("Something were wrong");
        }

    }

    private static void traineeMenu(User user, TraineeService traineeService, UserService userService, TrainingService trainingService) {
        System.out.println("TRAINEE MENU");
        System.out.println(CYAN+"Welcome @"+user.getUsername()+"!"+RESET);
        Trainee trainee = traineeService.existTraineeByUserId(user.getId());
        label:
        while(true) {

            System.out.println(PURPLE+"\nWhat do you want to do?"+RESET);
            traineeOptions();
            System.out.print("Type a number: ");
            String line = sc.nextLine();
            switch (line) {
                case "1":
                    //Change your password
                    chageUserPassword(user, userService);
                    trainee.setUser(user);
                    traineeService.updateTrainee(trainee);
                    break;
                case "2":
                    //Update profile
                    updateTraineeProfile(trainee, traineeService);
                    break;
                case "3":
                    //Active de-active profile
                    boolean active = traineeService.setIsActive(trainee);
                    if(!active){
                        System.out.println("You profile was desactive");
                    }else{
                        System.out.println("Your profile was active");
                    }
                    break;
                case "4":
                    //Delete profile by username
                    traineeService.deleteTraineeByUserName(user.getUsername());
                    user = null;
                    System.out.println("Profile deleted");
                    break;
                case "5":
                    //Training List
                    System.out.print("Enter start date (yyyy-mm-dd) or leave empty: ");
                    String startDateStr = sc.nextLine();
                    LocalDate startDate = startDateStr.isEmpty() ? null : LocalDate.parse(startDateStr);

                    System.out.print("Enter end date (yyyy-mm-dd) or leave empty: ");
                    String endDateStr = sc.nextLine();
                    LocalDate endDate = endDateStr.isEmpty() ? null : LocalDate.parse(endDateStr);

                    System.out.print("Enter trainer name or leave empty: ");
                    String trainerName = sc.nextLine();
                    trainerName = trainerName.isEmpty() ? null : trainerName;

                    System.out.println("Enter training type or leave empty: ");
                    String trainingTypeStr = sc.nextLine();
                    TrainingType trainingType = new TrainingType();
                    trainingType.setTypeName(trainingTypeStr);
                    List<Training> trainings = facade.getTrainingService().getTraineeTrainings(
                            trainee.getUser().getUsername(),
                            startDate,
                            endDate,
                            trainerName,
                            trainingType
                            );

                    System.out.println("Trainings List:");
                    for (Training training : trainings) {
                        System.out.println("Date: " + training.getTrainingDate() +
                                ", Trainee: " + training.getTrainee().getUser().getUsername() +
                                ", Type: " + training.getTrainingType().getTypeName());
                    }
                    break;
                case "6":
                    // Get trainers list that are not assigned on your profile
                    System.out.println("These are the trainers that are not associate with your profile");
                    List<Trainer> trainers = facade.getTrainerService().findAll();
                    List<Trainer> results = new ArrayList<>();
                    for (Trainer trainer : trainers){
                        boolean find = false;
                        for (Trainer t : trainee.getTrainers()){
                            if (t.getId() == trainer.getId() ){
                                find = true;
                                break;
                            }
                        }
                        if(!find){
                            results.add(trainer);
                        }
                    }
                    for (Trainer t: results){
                        System.out.println(t);
                    }
                    break;
                case "7":
                    //Update trainers list
                    while (true) {
                        System.out.println("Update your trainers list");
                        List<Trainer> trainerList = facade.getTrainerService().findAll();
                        List<Trainer> trainersNotAssigned = new ArrayList<>();

                        for (Trainer trainer : trainerList) {
                            boolean isAssigned = false;
                            for (Trainer assignedTrainer : trainee.getTrainers()) {
                                if (assignedTrainer.getId() == trainer.getId()) {
                                    isAssigned = true;
                                    break;
                                }
                            }
                            if (!isAssigned) {
                                trainersNotAssigned.add(trainer);
                            }
                        }

                        System.out.println("Available trainers to add:");
                        int index = 1;
                        for (Trainer trainer : trainersNotAssigned) {
                            System.out.println(index + ". " + trainer.getUser().getUsername() + " - " + trainer.getSpecialization());
                            index++;
                        }

                        // If no trainers are available to add, exit the loop
                        if (trainersNotAssigned.isEmpty()) {
                            System.out.println("No available trainers to add.");
                            break;
                        }

                        System.out.print("Select a trainer to add (or 0 to go back): ");
                        int selection;
                        try {
                            selection = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            continue;
                        }

                        if (selection == 0) {
                            break;
                        }

                        if (selection < 1 || selection > trainersNotAssigned.size()) {
                            System.out.println("Invalid selection");
                            continue;
                        }

                        Trainer selectedTrainer = trainersNotAssigned.get(selection - 1);
                        trainee.getTrainers().add(selectedTrainer);
                        selectedTrainer.getTrainees().add(trainee); // Ensure bidirectional relationship is maintained
                        System.out.println("Trainer added successfully");

                        facade.getTraineeService().updateTrainee(trainee);
                        facade.getTrainerService().updateTrainer(selectedTrainer); // Update the trainer as well

                    }
                    break;
                case "8":
                    break label;
                default:
                    nonDefineOption();
                    break;
            }
            if(line.equalsIgnoreCase("4")) break;
        }
    }

    private static void updateTraineeProfile(Trainee trainee, TraineeService traineeService) {
        System.out.println("Update your profile");
        while(true) {
            System.out.println("What do you want to update?" +
                    "\n1. Update name" +
                    "\n2. Update lastname" +
                    "\n3. Update date of birth" +
                    "\n4. Update address" +
                    "\n5. Back");
            System.out.print("Type the option number: ");
            String response = sc.nextLine();
            if (response.equals("1")) {
                System.out.print("Type the new name: ");
                String newName = sc.nextLine();
                trainee.getUser().setFirstName(newName);
            } else if (response.equals("2")) {
                System.out.print("Type the new lastname: ");
                String newLastname = sc.nextLine();
                trainee.getUser().setLastName(newLastname);
            } else if (response.equals("3")) {
                System.out.print("Type your year of birth: ");
                String year = validateYear(sc.nextLine());
                System.out.print("Type your month of birth: ");
                String month = validateMonth(sc.nextLine());
                System.out.print("Type your day of birth: ");
                String day = validateDay(year, month, sc.nextLine());
                LocalDate dateOfBirth = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                trainee.setDateOfBirth(dateOfBirth);
            } else if (response.equals("4")){
                System.out.print("Type the new address: ");
                String newAddress = sc.nextLine();
                trainee.setAddress(newAddress);
            } else if (response.equals("5")){
                break;
            }else {
                System.out.println("That is not a valid option \uD83E\uDD28 \nTry again!");
            }
            if(response.equals("1") || response.equals("2")){
                String newUsername = applicationContext.getBean(UserNameGenerator.class).generateUsername(trainee.getUser().getFirstName(), trainee.getUser().getLastName());
                if (newUsername.matches(trainee.getUser().getUsername()+"\\d*")){
                    newUsername = trainee.getUser().getUsername();
                }
                trainee.getUser().setUsername(newUsername);
            }
        }
        traineeService.updateTrainee(trainee);
    }

    private static void traineeOptions(){
        System.out.println("\nPress the option number depends on what do you wants to do:"+
                "\n1. Change your password"+
                "\n2. Update your Profile"+
                "\n3. Activate/De-activate your profile "+
                "\n4. Delete profile "+
                "\n5. Get Trainings list "+
                "\n6. Get trainers list that not assigned on your profile" +
                "\n7. Update trainers list" + RED +
                "\n8. Back"+RESET
        );
    }

    private static void createTrainer(TrainerService trainerService) {
        System.out.println("<---- Registe a new Trainer ---->");
        User user = createUser();
        System.out.print("Enter a Specialization: ");
        String specialization = sc.nextLine();
        TrainingType trainingType = new TrainingType();
        trainingType.setTypeName(specialization);
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(trainingType);
        System.out.println(CYAN+"Trainer created successfully!"+RESET);
        System.out.println("This is your generated username: "+trainer.getUser().getUsername());
        System.out.println("And this is your generated password: "+trainer.getUser().getPassword());
        long id = trainerService.createTrainer(trainer);
        System.out.println("Your id is: "+id);
    }

    private static User createUser (){
        System.out.print("Type name: ");
        String name = sc.nextLine();
        while(name.isEmpty()){
            System.out.print("You must to write a name. Try Again: ");
            name = sc.nextLine();
        }
        System.out.print("Type lastname: ");
        String lastname = sc.nextLine();
        while(lastname.isEmpty()){
            System.out.print("You must to write a lastname. Try Again: ");
            lastname = sc.nextLine();
        }
        String username = applicationContext.getBean(UserNameGenerator.class).generateUsername(name, lastname);
        String password = new PasswordGeneratorImpl().generatePassword(10);
        return new User(name, lastname, username, password, true);
    }

    private static void createTrainee(TraineeService traineeService) {
        System.out.println("<---- Registe a new Trainee ---->");
        User user = createUser();
        System.out.print("Type year of birth: ");
        String year = sc.nextLine();
        year = validateYear(year);


        System.out.print("Type month of birth: ");
        String month = sc.nextLine();

        month = validateMonth(month);


        System.out.print("Type day of birth: ");
        String day = sc.nextLine();

        day = validateDay(year, month, day);


        LocalDate dateOfBirth = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        System.out.print("Type the Address: ");
        String address = sc.nextLine();

        while (address.isEmpty()){
            System.out.print("You must type an address. Try again: ");
            address = sc.nextLine();
        }

        Trainee trainee = new Trainee(dateOfBirth,address);
        trainee.setUser(user);
        System.out.println(CYAN+"Trainee created successfully!"+RESET);
        System.out.println("This is your generated username: "+user.getUsername());
        System.out.println("And this is your generated password: "+user.getPassword());
        long id = traineeService.createTrainee(trainee);
        System.out.println("Your id is: "+id);
    }

    private static String validateDay(String year, String month, String day) {
        int maxDayOfMonth = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1).lengthOfMonth();
        while (Integer.parseInt(day) < 1 || Integer.parseInt(day) > maxDayOfMonth) {
            System.out.print("You must type a valid day for this month. Try again: ");
            day = sc.nextLine();
        }
        return day;
    }

    private static String validateMonth(String month) {
        while (!month.matches("0?[1-9]|1[0-2]")) {
            System.out.print("You must type a valid month (1-12). Try again: ");
            month = sc.nextLine();
        }
        return month;
    }

    private static String validateYear(String year) {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        while (!year.matches("\\d{4}") || Integer.parseInt(year) > currentYear - 14 || Integer.parseInt(year) < currentYear - 100) {
            System.out.print("You must type a valid year. Try again: ");
            year = sc.nextLine();
        }
        return year;
    }


    private static void displayMenu() {
        System.out.println("\nPress the option number depends on what do you wants to do:"+
                "\n1. Create new Trainee Profile"+
                "\n2. Create new Trainer Profile"+
                "\n3. Login"+ RED +
                "\n4. Exit"+RESET
        );
    }
    private static void nonDefineOption(){
        System.out.println("That is not a valid option \uD83E\uDD28 \nTry again!");
    }

    private static void mostrarBanner() {
        // Cargar el banner personalizado desde el archivo
        try (InputStream inputStream = App.class.getResourceAsStream("/banner.txt")) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                // Mostrar el contenido del banner en la consola
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(BLUE + line + RESET);
                }
            }
        } catch (IOException e) {
            // Manejar cualquier excepción al cargar o mostrar el banner
            logger.severe("There was an error loading the banner");
        }
    }
}
