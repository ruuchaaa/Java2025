package com.fo.model;

public class Order {
    public String customerName;
    public String items;
    public double total;
    public double discount;
    public String paymentMode;
    public String deliveryPartner;
    public double finalAmount;

    public Order(String customerName, String items, double total, double discount,
                 String paymentMode, String deliveryPartner, double finalAmount) {
        this.customerName = customerName;
        this.items = items;
        this.total = total;
        this.discount = discount;
        this.paymentMode = paymentMode;
        this.deliveryPartner = deliveryPartner;
        this.finalAmount = finalAmount;
    }

    public String getInvoice() {
        return "\n----- INVOICE -----\n"
                + "Customer: " + customerName + "\n"
                + "Items:\n" + items
                + "Total: ₹" + total + "\n"
                + "Discount: ₹" + discount + "\n"
                + "Payable: ₹" + finalAmount + "\n"
                + "Payment Mode: " + paymentMode + "\n"
                + "Delivery Partner: " + deliveryPartner + "\n-------------------";
    }
}
