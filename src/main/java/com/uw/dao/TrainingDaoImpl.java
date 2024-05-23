package com.uw.dao;

import com.uw.model.Training;
import com.uw.model.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TrainingDaoImpl implements TrainingDao{

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create( Training training ) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.persist(training);
            session.flush();
            transaction.commit();
        }catch( Exception e ) {
            transaction.rollback();
        }finally {
            session.close();
        }
    }

    @Override
    public Training selectProfile(long id) {
        Session session = sessionFactory.openSession();
        Training training = null;
        try {
            String hql = "FROM Training t WHERE t.id = :id";
            training = session.createQuery(hql, Training.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return training;
    }

    @Override
    public List<Training> findAll() {
        List<Training> trainings = null;
        try(Session session = sessionFactory.openSession()){
            trainings = session.createQuery("select t from Training t ", Training.class).getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return trainings;
    }

    @Override
    public List<Training> getTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType){
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Training> results = null;

        try {
            transaction = session.beginTransaction();

            StringBuilder queryStr = new StringBuilder("SELECT t FROM Training t WHERE t.trainee.user.username = :username");

            if (fromDate != null) {
                queryStr.append(" AND t.trainingDate >= :fromDate");
            }
            if (toDate != null) {
                queryStr.append(" AND t.trainingDate <= :toDate");
            }
            if (trainerName != null && !trainerName.isEmpty()) {
                queryStr.append(" AND t.trainer.user.firstName = :trainerName");
            }
            if (trainingType != null) {
                queryStr.append(" AND t.trainingType.typeName = :trainingType");
            }

            Query<Training> query = session.createQuery(queryStr.toString(), Training.class);
            query.setParameter("username", username);

            if (fromDate != null) {
                query.setParameter("fromDate", fromDate);
            }
            if (toDate != null) {
                query.setParameter("toDate", toDate);
            }
            if (trainerName != null && !trainerName.isEmpty()) {
                query.setParameter("trainerName", trainerName);
            }
            if (trainingType != null) {
                query.setParameter("trainingType", trainingType.getTypeName());
            }

            results = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return results;
    }

    @Override
    public List<Training> getTrainerTrainings(String username, LocalDate startDate, LocalDate endDate, String traineeName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Training> results = null;

        try {
            transaction = session.beginTransaction();

            StringBuilder queryStr = new StringBuilder("SELECT t FROM Training t WHERE t.trainer.user.username = :username");

            if (startDate != null) {
                queryStr.append(" AND t.trainingDate >= :fromDate");
            }
            if (endDate != null) {
                queryStr.append(" AND t.trainingDate <= :toDate");
            }
            if (traineeName != null && !traineeName.isEmpty()) {
                queryStr.append(" AND t.trainee.user.firstName = :traineeName");
            }

            Query<Training> query = session.createQuery(queryStr.toString(), Training.class);
            query.setParameter("username", username);

            if (startDate != null) {
                query.setParameter("fromDate", startDate);
            }
            if (endDate != null) {
                query.setParameter("toDate", endDate);
            }
            if (traineeName != null && !traineeName.isEmpty()) {
                query.setParameter("traineeName", traineeName);
            }

            results = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("No se encontró entrenamiento con estas characteristics");
        } finally {
            session.close();
        }

        return results;
    }
}
