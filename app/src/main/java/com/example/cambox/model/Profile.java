package com.example.cambox.model;

public class Profile {
    String name;
    String address;
    String dob;
    String gender;

    public Profile() {
    }

    public Profile(String name) {
        this.name = name;
        address = "address not set";
        dob = "date not set";
        gender = "Male";
    }

    public Profile(String name, String address, String dob, String gender) {
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
