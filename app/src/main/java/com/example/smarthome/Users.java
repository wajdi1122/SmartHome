package com.example.smarthome;

public class Users {

    String firstName;
    String cin, phone;

    public Users() {
    }

    public Users(String firstName, String cin , String phone) {
        this.firstName = firstName;
        this.cin = cin;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}