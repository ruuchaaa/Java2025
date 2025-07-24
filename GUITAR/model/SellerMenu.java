package com.ap.GUITAR.model;

import java.util.Scanner;
import java.util.UUID;

public class SellerMenu {
    public static void handleSeller(Scanner scanner, Inventory inventory) {
        while (true) {
            System.out.println("\n--- Seller Menu ---");
            System.out.println("1. Add Guitar");
            System.out.println("2. Remove Guitar");
            System.out.println("3. List All Guitars");
            System.out.println("4. Back");
            System.out.print("Choose: ");
            int ch = scanner.nextInt();
            scanner.nextLine();

            switch (ch) {
                case 1 -> {
                    String serial = UUID.randomUUID().toString();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble(); scanner.nextLine();

                    Builder builder = promptEnum(scanner, Builder.class, "Builder");
                    System.out.print("Enter model: ");
                    String model = scanner.nextLine();
                    Type type = promptEnum(scanner, Type.class, "Type");
                    Wood backWood = promptEnum(scanner, Wood.class, "BackWood");
                    Wood topWood = promptEnum(scanner, Wood.class, "TopWood");
                    System.out.print("Enter number of strings: ");
                    int strings = scanner.nextInt(); scanner.nextLine();

                    inventory.addGuitar(serial, price, builder, model, type, backWood, topWood, strings);
                    System.out.println("✅ Guitar added successfully.");
                }
                case 2 -> {
                    System.out.print("Enter serial number to remove: ");
                    String serial = scanner.nextLine();
                    boolean removed = inventory.removeGuitar(serial);
                    System.out.println(removed ? "✅ Removed." : "❌ Not found.");
                }
                case 3 -> inventory.listAllGuitars();
                case 4 -> { return; }
                default -> System.out.println("Invalid.");
            }
        }
    }

    private static <T extends Enum<T>> T promptEnum(Scanner scanner, Class<T> enumClass, String label) {
        T[] values = enumClass.getEnumConstants();
        System.out.println("Select " + label + ":");
        for (int i = 0; i < values.length; i++) {
            System.out.println((i + 1) + ". " + values[i]);
        }
        int choice = scanner.nextInt(); scanner.nextLine();
        return values[choice - 1];
    }
}