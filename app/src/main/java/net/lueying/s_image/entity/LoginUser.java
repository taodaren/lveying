package net.lueying.s_image.entity;

import java.io.Serializable;

public class LoginUser implements Serializable {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "token='" + token + '\'' +
                '}';
    }
}
