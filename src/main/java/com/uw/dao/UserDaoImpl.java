package com.uw.dao;

import com.uw.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Repository
public class UserDaoImpl implements UserDao {

    private SessionFactory sessionFactory;
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class.getName());

    @Autowired
    public void setSessionFactory( SessionFactory sessionFactory ){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean validateUsername(String username) {
        User user = null;
        try(Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("select u from User u where u.username = :username", User.class).setParameter("username", username);
            user = query.getSingleResult();
        }catch( Exception e ){
            logger.severe("Failed to validate user");
            logger.severe(e.getMessage());
        }
        return user != null;
    }

    @Override
    public boolean validatePassword(String username, String password) {
        User user = null;
        try(Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("select u from User u where u.username = :username and u.password = :password", User.class).setParameter("username", username).setParameter("password", password);
            user = query.getSingleResult();
        }catch( Exception e ){
            logger.severe("Failed to validate user");
            logger.severe(e.getMessage());
        }
        return user != null;
    }

    @Override
    public User findUserByUsername(String username) {
        User user = null;
        try(Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("select u from User u where u.username = :username", User.class).setParameter("username", username);
            user = query.getSingleResult();
        }catch( Exception e ){
            logger.severe("Failed to get user by username");
            logger.severe(e.getMessage());
        }
        return user;
    }

    @Override
    public boolean updateUser(User user) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            return true;
        } catch( Exception e ){
            if(transaction != null){
                transaction.rollback();
            }
            logger.severe("Failed to update user");
            logger.severe(e.getMessage());
        }
        return false;
    }

    @Override
    public List<User> findAll() {
        List<User> usersList = null;

        try( Session session = sessionFactory.openSession() ){
            Query<User> query = session.createQuery("select u from User u", User.class);
            usersList = query.getResultList();
        } catch ( Exception e ){
            logger.severe("Failed to find users");
            logger.severe(e.getMessage());
        }

        return usersList;
    }

}
