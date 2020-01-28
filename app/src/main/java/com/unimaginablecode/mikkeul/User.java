package com.unimaginablecode.mikkeul;

public class User {
    private Integer premium = 2;
    private Integer validity;
    private String name;
    private  String parentalContact;
    private  String parentalRelationship;
    private String parentalName;

    public User() {
    }

    public User(Integer premium, Integer validity, String name, String parentalContact, String parentalRelationship, String parentalName) {
        this.premium = premium;
        this.validity = validity;
        this.name = name;
        this.parentalContact = parentalContact;
        this.parentalRelationship = parentalRelationship;
        this.parentalName = parentalName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPremium() {
        return premium;
    }

    public Integer getValidity() {
        return validity;
    }

    public String getName() {
        return name;
    }

    public String getParentalContact() {
        return parentalContact;
    }

    public String getParentalRelationship() {
        return parentalRelationship;
    }

    public String getParentalName() {
        return parentalName;
    }
}
