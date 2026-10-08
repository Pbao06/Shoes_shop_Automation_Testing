package com.example.models;

public class Checkout {
    private  String firstName; 
    private String lastName;
    private String email;
    private String address;
    private String city;
    private String postalCode;
    private String country;

    private String paymentMethod;
    private String expectedErrorMessage;

    public Checkout(String firstName, String lastName,String email, String address, String city, 
                    String postalCode, String country, String paymentMethod,String expectedErrorMessage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email=email;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.paymentMethod = paymentMethod;
        this.expectedErrorMessage=expectedErrorMessage;
    }
    public String getFirstName(){return this.firstName;}
    public String getLastName(){return this.lastName;}
    public String getEmail(){return this.email;}
    public String getAddress(){return this.address;}
    public String getCity(){return this.city;}
    public String getPostalCode(){return this.postalCode;}
    public String getCountry(){return this.country;}
    public String getPaymentMethod(){return this.paymentMethod;}
    public String getErrorMessage(){return this.expectedErrorMessage;}

}
