package com.herovired.Auction.Management.System.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, updatable = false)
    private long id;


    @Column(name = "user_id", unique = true, updatable = false)
    @NotBlank
    @Size(min = 5, max = 300, message = "Length should be in between 5 to 300")
    private String userId;

    @Column(name = "user_name", updatable = false, unique = true)
    @Size(min = 5, max = 30, message = "Length should be in between 5 to 30")
    private String userName;

    @Column(name = "name", updatable = true, unique = false)
    @NotBlank
    @Size(min = 5, max = 30, message = "Length should be in between 5 to 30")
    private String name;

    @Column(name = "user_password", updatable = true, unique = false)
    @NotBlank
    @Size(min = 6, max = 300, message = "Length should be in between 6 to 30")
    private String userPassword;

    @Column(name = "email", updatable = true, unique = true)
    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "is_email_public")
    private boolean isEmailPublic;

    @Column(name = "phone_number", updatable = true, unique = true)
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Invalid phone number")
    private String phoneNumber;

    @Column(name = "is_phone_public")
    private boolean isPhonePublic;

    @Column(name = "upi_id", updatable = true, unique = true)
    @Size(min = 6, max = 50, message = "Length should be in between 6 to 50")
    private String upiId;

    @Column(name = "is_upi_public")
    private boolean isUpiPublic;

    @Column(name = "address", updatable = true, unique = false)
    @Size(min = 2, max = 100, message = "Length should be in between 2 to 100")
    private String address;

    @Column(name = "is_address_public")
    private boolean isAddressPublic;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailPublic() {
        return isEmailPublic;
    }

    public void setEmailPublic(boolean emailPublic) {
        isEmailPublic = emailPublic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isPhonePublic() {
        return isPhonePublic;
    }

    public void setPhonePublic(boolean phonePublic) {
        isPhonePublic = phonePublic;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public boolean isUpiPublic() {
        return isUpiPublic;
    }

    public void setUpiPublic(boolean upiPublic) {
        isUpiPublic = upiPublic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAddressPublic() {
        return isAddressPublic;
    }

    public void setAddressPublic(boolean addressPublic) {
        isAddressPublic = addressPublic;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", email='" + email + '\'' +
                ", isEmailPublic=" + isEmailPublic +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isPhonePublic=" + isPhonePublic +
                ", upiId='" + upiId + '\'' +
                ", isUpiPublic=" + isUpiPublic +
                ", address='" + address + '\'' +
                ", isAddressPublic=" + isAddressPublic +
                '}';
    }
}
