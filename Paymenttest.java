
package com.ap.test11;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.ap.model11.Creditcard;
import com.ap.model11.Debitcard;
import com.ap.model11.PaymentGateway;
import com.ap.model11.UPI;

public class Paymenttest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PaymentGateway gateway = null;

        while (true) {
            try {
                System.out.print("Enter amount to pay (or type 'exit' to quit): ");
                String amountInput = scanner.next();

                if (amountInput.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting payment system.");
                    break;
                }

                double amount = Double.parseDouble(amountInput);
                if (amount <= 0) {
                    throw new IllegalArgumentException("Amount must be greater than 0.");
                }

                System.out.println("\nSelect payment method:");
                System.out.println("1. Credit Card");
                System.out.println("2. Debit Card");
                System.out.println("3. UPI");
                System.out.println("4. Exit");
                System.out.print("Enter option: ");

                String methodInput = scanner.next();
                if (methodInput.equalsIgnoreCase("exit") || methodInput.equals("4")) {
                    System.out.println("Exiting payment system.");
                    break;
                }

                int option = Integer.parseInt(methodInput);

                switch (option) {
                    case 1 -> gateway = new Creditcard();
                    case 2 -> gateway = new Debitcard();
                    case 3 -> gateway = new UPI();
                    default -> throw new IllegalArgumentException("Invalid payment option.");
                }

                System.out.println("Using: " + gateway.getClass().getSimpleName());
                gateway.pay(amount);

                scanner.nextLine(); 

                System.out.print("\nWas the product faulty? (yes/no/exit): ");
                String productIssue = scanner.nextLine();

                if (productIssue.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting payment system.");
                    break;
                } else if (productIssue.equalsIgnoreCase("yes")) {
                    System.out.println("Product reported faulty. Processing refund...");
                    gateway.refund(amount);
                } else {
                    System.out.println("Product accepted. No refund issued.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Input error: Please enter a valid numeric value.");
                scanner.nextLine(); 
            } catch (InputMismatchException e) {
                System.out.println("Input error: Please enter valid numeric input.");
                scanner.nextLine(); 
            } catch (IllegalArgumentException e) {
                System.out.println("Validation error: " + e.getMessage());
                scanner.nextLine(); 
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e);
                scanner.nextLine(); 
            } finally {
                System.out.println("Transaction completed.\n");
            }

            System.out.print("Do you want to make another transaction? (yes/no): ");
            String again = scanner.nextLine();
            if (again.equalsIgnoreCase("no") || again.equalsIgnoreCase("exit")) {
                System.out.println("Thank you for using our payment system.");
                break;
            }
        }

        scanner.close();
    }
}
