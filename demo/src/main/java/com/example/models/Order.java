package com.example.models;
public class Order
{ 
    // need to check exacly -> status , name and size , quantity , total price 
    private String state;
    private String name;
    private int size;
    private int quantity;
    private double totalPrice;

    // Constructor with parameters
    public Order(String state, String name, int size, int quantity, double totalPrice) {
        this.state = state;
        this.name = name;
        this.size = size;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
    
    // Getters
    public String getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // Setters
    public void setState(String state) {
        this.state = state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}