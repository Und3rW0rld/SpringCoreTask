package com.uw.repository;

import com.uw.exceptions.AuthenticationErrorException;
import com.uw.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Repository
public class UserRepository {
    private static final Logger logger = Logger.getLogger(UserRepository.class.getName());
    @PersistenceContext
    private EntityManager entityManager;

    public Boolean usernameExists(String username) {
        Long count = (Long) entityManager.createQuery("select count(u) from User u where u.username like :username")
                .setParameter("username", username)
                .getSingleResult();
        logger.info("Count - " + count);
        return count > 0;
    }

    @Transactional
    public User save(User user) {
        logger.info("User saved successfully");
        return entityManager.merge(user);
    }

    @Transactional
    public boolean authenticate(String username, String password) throws AuthenticationException {
        if (usernameExists(username)) {
            User user = (User) entityManager.createQuery("from User u where u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();
            if (BCrypt.checkpw(password,user.getPassword())) {
                logger.info("User authenticated successfully");
                return true;
            } else {
                logger.warning("Wrong Credentials");
                throw new AuthenticationErrorException();
            }
        } else {
            logger.warning("User not found");
            throw new EntityNotFoundException();
        }
    }
    public Optional<User> findById(Long id) {
        User user = (User) entityManager.createQuery("from User u where u.id = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (user == null) throw new EntityNotFoundException();

        return Optional.of(user);
    }

    public Optional<User> findByUsername(String username) {
        User user;
        if (usernameExists(username)) {
            user = (User) entityManager.createQuery("from User u where u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();
        } else {
            throw new EntityNotFoundException();
        }
        return Optional.of(user);
    }

    @Transactional
    public void changePassword(String newPassword, Long userId) {
        entityManager.createQuery("update User u set u.password = :newPassword where u.id = :userId")
                .setParameter("newPassword", newPassword)
                .setParameter("userId", userId)
                .executeUpdate();
        logger.info("Password changed successfully");
    }

    @Transactional
    public void activateDeactivate(Boolean isActive, Long userId) {
        entityManager.createQuery("update User u set u.isActive = :active where u.id = :userId")
                .setParameter("active", isActive)
                .setParameter("userId", userId)
                .executeUpdate();
        logger.info("User activated/deactivated successfully");
    }

    @Transactional
    public void removeUser(String username){
        entityManager.createQuery("delete User u where u.username = :username")
                .setParameter("username",username)
                .executeUpdate();
    }

    public Optional<List<User>> findAll() {
        List<User> users = entityManager.createQuery("select u from User u", User.class).getResultList();
        if (users.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(users);
        }
    }

}