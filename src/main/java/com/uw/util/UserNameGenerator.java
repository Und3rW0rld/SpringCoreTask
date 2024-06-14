package com.uw.util;

import com.uw.model.User;
import com.uw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserNameGenerator {

    private UserService userServiceImpl;

    @Autowired
    public void setUserServiceImpl(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    public String generateUsername( String firstName, String lastName ) {
        String username = firstName + "." + lastName;
        int cont = 0;
        if(!userServiceImpl.findAll().isEmpty()){
            System.out.println(userServiceImpl.findAll());
            for( User value: userServiceImpl.findAll()){
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