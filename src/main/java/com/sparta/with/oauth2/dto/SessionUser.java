package com.sparta.with.oauth2.dto;


import com.sparta.with.entity.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user){
        this.name = user.getUsername();
        this.email = user.getEmail();
        this.picture = user.getImage();
    }
}
