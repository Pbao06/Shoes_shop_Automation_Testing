package com.example.models;

public class CheckoutData {

    private String name;
    private int quantity;
    private String size;
    private String totalPrice;

    public CheckoutData(String name, int quantity, String size,String totalPrice) {
        this.name = name;
        this.quantity = quantity;
        this.size = size;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }
    public int getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

    public String getTotalPrice() {
        return totalPrice;
    }
}
