package com.example.zayndatcostume.models;

import java.util.List;

public class Costume {
    private String id;
    private String name;
    private String imageLink;
    private String price;
    private String rentalFee;
    private List<String> category;
    private String publisher;
    private List<String> size;
    private int availableQuantity;

    public Costume(String id, String name, String imageLink, String price, String rentalFee, List<String> category, String publisher, List<String> size, int availableQuantity) {
        this.id = id;
        this.name = name;
        this.imageLink = imageLink;
        this.price = price;
        this.rentalFee = rentalFee;
        this.category = category;
        this.publisher = publisher;
        this.size = size;
        this.availableQuantity = availableQuantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRentalFee() {
        return rentalFee;
    }

    public void setRentalFee(String rentalFee) {
        this.rentalFee = rentalFee;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    @Override
    public String toString() {
        return "Costume{" +
                "name='" + name + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", price='" + price + '\'' +
                ", rentalFee='" + rentalFee + '\'' +
                ", category=" + category +
                ", publisher='" + publisher + '\'' +
                ", size=" + size +
                ", availableQuantity=" + availableQuantity +
                '}';
    }
}
