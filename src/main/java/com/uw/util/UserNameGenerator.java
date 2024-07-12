package com.uw.util;

import com.uw.model.User;
import com.uw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserNameGenerator {

    private UserRepository userRepository;

    @Autowired
    public void setUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateUsername( String firstName, String lastName ) {
        String username = firstName + "." + lastName;
        int cont = 0;
        if(userRepository.findAll().isPresent()){
            System.out.println(userRepository.findAll());
            for( User value: userRepository.findAll().get()){
                if( value.getFirstName().equalsIgnoreCase(firstName)
                        && value.getLastName().equalsIgnoreCase(lastName)){
                    cont++;
                }
            }
        }
        if ( cont == 0 ){
            return username;
        }else{
            return username+cont;
        }
    }

}