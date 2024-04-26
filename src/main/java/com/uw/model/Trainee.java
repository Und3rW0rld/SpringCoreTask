package com.uw.model;

import java.time.LocalDate;

public class Trainee extends User{

    private LocalDate dateOfBirth;
    private String adress;
    private long userId;

    public Trainee(String firstName, String lastName, String username, String password, boolean isActive, LocalDate dateOfBirth, String adress) {
        super(firstName, lastName, username, password, isActive);
        this.dateOfBirth = dateOfBirth;
        this.adress = adress;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAdress() {
        return adress;
    }

    @Override
    public String toString() {
        return  super.toString()+" [" +
                "dateOfBirth=" + dateOfBirth +
                ", adress='" + adress + '\'' +
                ", userId=" + userId +
                "] " ;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return this.userId;
    }
}
