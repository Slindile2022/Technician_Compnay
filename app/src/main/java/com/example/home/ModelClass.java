package com.example.home;

public class ModelClass {

    private String productTitle;
    private String productDescription;
    private  String productIconTv;
    private  String timeStamp;

    public ModelClass() {
    }

    public ModelClass(String productTitle, String productDescription, String productIconTv, String timeStamp) {
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.productIconTv = productIconTv;
        this.timeStamp = timeStamp;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getTimeStamp() {
        return timeStamp;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductIconTv() {
        return productIconTv;
    }

    public void setProductIconTv(String productIconTv) {
        this.productIconTv = productIconTv;
    }
}
