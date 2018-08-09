package net.lueying.s_image.entity;

/**
 * 登录和注册返回的实体一样
 */
public class RegisterEntity {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "RegisterEntity{" +
                "token='" + token + '\'' +
                '}';
    }
}
