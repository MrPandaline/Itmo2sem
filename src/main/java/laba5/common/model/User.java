package laba5.common.model;

import java.io.Serializable;

public class User implements Serializable {
    private long id;
    private String login;
    private String password;

    public User(int id, String login, String password) {
        this(login, password);
        this.id = id;

    }

    public User( String login, String password) {
        this.login = login;
        this.password = password;
    }

    public long id() { return id; }

    public String login() { return login;}

    public String password() { return password;}

    public void id(long id) { this.id = id; }

    public void login(String login) { this.login = login; }

    public void password(String password) { this.password = password; }
}
