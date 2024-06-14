package com.uw.mapper;

import com.uw.dto.UserDTO;
import com.uw.model.User;

public class UserMapper {

    public static UserDTO ToDTO (User user){
        if( user == null ){
            return null;
        }
        return new UserDTO(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getPassword(),
                user.isActive());
    }

    public static User toEntity(UserDTO userDTO){
        User user = new User(
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.isActive());
        user.setId(userDTO.getId());
        return user;
    }
}
