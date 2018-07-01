package onecourse.models;

public class User {
    private int id;
    private String email;
    private String password;
    private String role;

    public User() {

    }

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(User user, int id) {
        this.id = id;
        this.email = user.email;
        this.password = user.password;
        this.role = user.role;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUniqueValue() {
        return this.getEmail();
    }
}
