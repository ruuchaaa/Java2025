package com.ap.model11;

public interface PaymentGateway {
    void pay(double amount);
    void refund(double amount);
}

