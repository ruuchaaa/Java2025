package com.ap.model11;

public class Debitcard implements PaymentGateway {


    @Override
    public void pay(double amount) {
        System.out.println("Processing Net Banking payment of ₹" + amount);
        System.out.println("Net Banking payment successful.");
    }

    @Override
    public void refund(double amount) {
        System.out.println("Processing Net Banking refund of ₹" + amount);
        System.out.println("Net Banking refund successful.");
    }
}