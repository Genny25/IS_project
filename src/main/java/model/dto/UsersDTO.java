package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class UsersDTO implements Serializable {
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private Roles role;


    public enum Roles {
        Admin,
        CastingDirector,
        ProductionManager,
        Performer
    }

    public UsersDTO() {
    }

    public UsersDTO(int usersID, String firstName, String lastName, String email, String passwordHash, Roles role) {
        this.userID = usersID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UsersDTO{" +
                "usersID=" + userID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UsersDTO usersDTO = (UsersDTO) o;
        return userID == usersDTO.userID && Objects.equals(firstName, usersDTO.firstName) && Objects.equals(lastName, usersDTO.lastName) && Objects.equals(email, usersDTO.email) && Objects.equals(passwordHash, usersDTO.passwordHash) && role == usersDTO.role;
    }
}
