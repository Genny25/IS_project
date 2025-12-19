package model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class UserDTO implements Serializable {
    private int userID;
    private String firstName;
    private String lastName;
    private String passwordHash;
    private String phoneNumber;
    private Role role;
    private String email;
    private LocalDateTime registrationDate;

    public UserDTO() {}

    public UserDTO(int userID, String firsName, String lastName, String passwordHash, String phoneNumber, Role role, String email, LocalDateTime registrationDate) {
        this.userID = userID;
        this.firstName = firsName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.email = email;
        this.registrationDate = registrationDate;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userID=" + userID +
                ", firsName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return userID == userDTO.userID && Objects.equals(firstName, userDTO.firstName) && Objects.equals(lastName, userDTO.lastName) && Objects.equals(passwordHash, userDTO.passwordHash) && Objects.equals(phoneNumber, userDTO.phoneNumber) && role == userDTO.role && Objects.equals(email, userDTO.email) && Objects.equals(registrationDate, userDTO.registrationDate);
    }

    public enum Role {
        Performer,
        CastingDirector,
        ProductionManager
    }
}
