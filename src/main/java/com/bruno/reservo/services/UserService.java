package com.bruno.reservo.services;

import com.bruno.reservo.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User getLoggedUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public Long getLoggedUserId() {
        User user = getLoggedUser();
        return user.getId();
    }
}
