package model;
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private Enums.Role role;

    public User(String username, String password, Enums.Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Enums.Role getRole() { return role; }
}