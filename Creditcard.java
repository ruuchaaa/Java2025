package com.ap.model11;

public class Creditcard implements PaymentGateway {

    @Override
    public void pay(double amount) {
        System.out.println("Processing Credit Card payment of ₹" + amount);
        System.out.println("Credit Card payment successful.");
    }

    @Override
    public void refund(double amount) {
        System.out.println("Processing Credit Card refund of ₹" + amount);
        System.out.println("Credit Card refund successful.");
    }
}
