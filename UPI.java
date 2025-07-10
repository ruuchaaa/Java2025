package com.ap.model11;

public class UPI implements PaymentGateway {

    @Override
    public void pay(double amount) {
        System.out.println("Processing UPI payment of ₹" + amount);
        System.out.println("UPI payment successful.");
    }

    @Override
    public void refund(double amount) {
        System.out.println("Processing UPI refund of ₹" + amount);
        System.out.println("UPI refund successful.");
    }
}
