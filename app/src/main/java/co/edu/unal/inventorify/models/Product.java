package co.edu.unal.inventorify.models;

/**
 * Inventorify
 * Created by Jhon Ramirez on 8/29/17.
 * Universidad Nacional de Colombia
 */
public class Product {

    public String uid;
    public String name;
    public String barcode;
    public int quantity;
    public int price;
    public int cost;
    public String imageURL;

    public Product() {}

    public Product(String uid){
        this.uid = uid;
    }

    public void setUid(String uid) {this.uid = uid;}

    public void setName(String name){
        this.name = name;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(int price){this.price = price;}

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getUid() {return uid;}

    public String getName() {
        return name;
    }

    public String getBarcode() {
        return barcode;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getCost() {
        return cost;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
