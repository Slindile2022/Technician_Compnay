package com.example.home;

public class ProductClass {


    private String productTitle;
    private String productDescription;
    private String productCategory;
    private  String productIconTv;
    private String timeStamp;



    public ProductClass() {
    }

    public ProductClass(String timeStamp, String productTitle, String productDescription, String productCategory, String productIconTv) {
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.productIconTv = productIconTv;
        this.timeStamp = timeStamp;



    }






    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }



    public String getProductTitle() {
        return productTitle;
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

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductIconTv() {
        return productIconTv;
    }

    public void setProductIconTv(String productIconTv) {
        this.productIconTv = productIconTv;
    }
}
