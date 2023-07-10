package com.example.home;

public class SubmitOrderUserInfo {

    String orderId, orderTime, orderBy,customerPhone,id, orderStatus, price, technician, technicianId;

    public SubmitOrderUserInfo() {
    }

    public SubmitOrderUserInfo(String orderId, String orderTime, String orderBy, String customerPhone, String id, String orderStatus, String price, String technician, String technicianId) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.orderBy = orderBy;
        this.customerPhone = customerPhone;
        this.id = id;
        this.orderStatus = orderStatus;
        this.price = price;
        this.technician = technician;
        this.technicianId = technicianId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }
}
