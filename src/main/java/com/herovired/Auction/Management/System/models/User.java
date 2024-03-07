package com.herovired.Auction.Management.System.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Set;

@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, updatable = false)
    private long id;

    @Column(name = "user_id", unique = true, updatable = false)
    @NotBlank
    @Size(min = 5, max = 300, message = "Length should be in between 5 to 300")
    private String userId;

    @Column(name = "email", updatable = true, unique = true)
    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "is_admin")
    @NotNull
    private boolean isAdmin;

    @Column(name = "user_name", updatable = false, unique = true)
    @NotBlank
    @Size(min = 5, max = 300, message = "Length should be in between 5 to 30")
    private String userName;

    @Column(name = "name", updatable = true, unique = false)
    @NotBlank
    @Size(min = 5, max = 30, message = "Length should be in between 5 to 30")
    private String name;



    @Column(name = "user_password", updatable = true, unique = false)
    @NotBlank
    @Size(min = 6, max = 300, message = "Length should be in between 6 to 30")
    private String userPassword;



    @Column(name = "is_blocked", updatable = true)
    private boolean isBlocked = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Authority> authorities;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<UserAuctionRegistration> registrations;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserAuctionRegistration> getRegistrations() {
        return registrations;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setRegistrations(Set<UserAuctionRegistration> registrations) {
        this.registrations = registrations;
    }

//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", userId='" + userId + '\'' +
//                ", email='" + email + '\'' +
//                ", isAdmin=" + isAdmin +
//                ", userName='" + userName + '\'' +
//                ", name='" + name + '\'' +
//                ", userPassword='" + userPassword + '\'' +
//                ", isBlocked=" + isBlocked +
//                '}';
//    }
    @Override
    public String toString() {
        return "User{" +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", isBlocked=" + isBlocked +
                '}';
    }
}