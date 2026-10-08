package com.example.models.admin;

public class ProductEditClass {
    private String productName; 
    private String slug;
    private String description;
    private double price;
    private double salePrice;
    private String color;
    private String brand;
    private String category;
    private String status;
    private String img;


    // Parameterized constructor
    public ProductEditClass(String productName, String slug, String description, double price, 
                       double salePrice, String color, String brand, String category, 
                       String status, String img) {
        this.productName = productName;
        this.slug = slug;
        this.description = description;
        this.price = price;
        this.salePrice = salePrice;
        this.color = color;
        this.brand = brand;
        this.category = category;
        this.status = status;
        this.img = img;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

