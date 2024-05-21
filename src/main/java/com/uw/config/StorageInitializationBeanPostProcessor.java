package com.uw.config;

import com.uw.model.Trainee;
import com.uw.model.Trainer;
import com.uw.model.Training;
import com.uw.model.TrainingType;
import com.uw.service.TraineeService;
import com.uw.service.TrainerService;
import com.uw.service.TrainingService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Properties;
import java.util.logging.Logger;

@Component
@PropertySource("classpath:application.properties")
public class StorageInitializationBeanPostProcessor implements BeanPostProcessor {

    private final ResourceLoader resourceLoader;
    private static final Logger logger = Logger.getLogger(StorageInitializationBeanPostProcessor.class.getName());


    public StorageInitializationBeanPostProcessor(ResourceLoader resourceLoader){
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof TraineeService || bean instanceof TrainerService || bean instanceof  TrainingService){
            initializeStorage(bean);
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    private void initializeStorage(Object storage){
        String propertyValue = "";
        try {
            Resource resource = resourceLoader.getResource("classpath:application.properties");
            InputStream inputStream = resource.getInputStream();
            Properties properties = new Properties();
            properties.load(inputStream);

            propertyValue = properties.getProperty("data.file.path");
            if (propertyValue.isEmpty()) {
                logger.severe("The property is empty or wasn't specified");
                return;
            }
        } catch (IOException e) {
            logger.severe(String.format("Something went wrong: %s", e.getMessage()));
        }

        Resource resource = resourceLoader.getResource(propertyValue);
        try ( BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            int registers = 0;
            while( (line = reader.readLine()) != null ){
                if (line.equals("Trainee_Data") && storage instanceof TraineeService){
                    while(!(line = reader.readLine()).equals("Trainer_Data")){
                        String [] parts = line.split(":");
                        String[] data = parts[1].split(",");
                        Trainee trainee = new Trainee(data[0], data[1], data[2], data[3], Boolean.parseBoolean(data[4]), LocalDate.parse(data[5]), data[6]);
                        trainee.setUserId(Long.parseLong(parts[0]));
                        ( (TraineeService) storage ).createTrainee(trainee);
                        registers++;
                    }
                    logger.info(String.format("Added: %s trainees from datasource", registers));
                } else if (line.equals("Trainer_Data") && storage instanceof TrainerService) {
                while(!(line = reader.readLine()).equals("Training_Data")){
                    String [] parts = line.split(":");
                    String[] data = parts[1].split(",");
                    Trainer trainer = new Trainer(data[0], data[1], data[2], data[3], Boolean.parseBoolean(data[4]), data[5]);
                    trainer.setUserId(Long.parseLong(parts[0]));
                    ((TrainerService) storage).createTrainer(trainer);
                    registers++;
                }
                logger.info(String.format("Added: %s trainees from datasource", registers));
            } else if (line.equals("Training_Data") && storage instanceof TrainingService) {
                while((line = reader.readLine()) != null){
                    String [] parts = line.split(":");
                    String[] data = parts[1].split(",");
                    Training training = new Training(Long.parseLong(data[0]), Long.parseLong(data[1]), data[2], new TrainingType(data[3]), LocalDate.parse(data[4]), data[5]);
                    training.setId(Long.parseLong(parts[0]));
                    ((TrainingService) storage).createTraining(training);
                    registers++;
                }
                logger.info(String.format("Added: %s trainees from datasource", registers));
            }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
