package com.dbest.sevs.entity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.persistence.*;
import java.util.Date;


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstname;
    private String lastname;
    private String middlename;
    @Column(unique=true)
    private String identifier;
    private String password;
    private Date dateCreated;
    private Date lastModified;
    @OneToOne
    private Token token;
    @Column(unique=true)
    private String phone;
    @Column(unique=true)
    private String emailAddress;
    private boolean changePassword;
    private boolean blocked;
    private int trialCount;

    public User(long id) {
        this.id = id;
        this.trialCount=0;
    }
    public User() {
        this.trialCount=0;
    }

    public User(String emailAddress, String phone) {
        this.emailAddress=emailAddress;
        this.phone=phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public int getTrialCount() {
        return trialCount;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setTrialCount(int trialCount) {
        this.trialCount = trialCount;
    }
    public void resetCount(){
        this.trialCount=0;
    }
}
