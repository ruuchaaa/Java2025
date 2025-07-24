package com.ap.GUITAR.test;

import java.util.List;
import java.util.Scanner;

import com.ap.GUITAR.model.*;

public class GuitarApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Inventory inventory = new Inventory();
        preloadGuitars(inventory);  

        while (true) {
            System.out.println("\n Welcome to the Guitar Inventory System ");
            System.out.println("1. Seller Login");
            System.out.println("2. Customer View");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int mainChoice = getIntInput(scanner, 1, 3);

            switch (mainChoice) {
                case 1 -> sellerMenu(scanner, inventory);
                case 2 -> customerMenu(scanner, inventory);
                case 3 -> {
                    System.out.println("Thank you for using the system!");
                    scanner.close();
                    return;
                }
            }
        }
    }
    
    
    

    private static void sellerMenu(Scanner scanner, Inventory inventory) {
        while (true) {
            System.out.println("\n--- Seller Menu ---");
            System.out.println("1. Add Guitar");
            System.out.println("2. View All Guitars");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");
            int sellerChoice = getIntInput(scanner, 1, 3);

            switch (sellerChoice) {
                case 1 -> {
                    System.out.print("Enter Serial Number: ");
                    String serial = scanner.nextLine();
                    System.out.print("Enter Price: ");
                    double price = Double.parseDouble(scanner.nextLine());

                    Builder builder = promptEnum(scanner, Builder.class, "Select Builder");
                    System.out.print("Enter Model: ");
                    String model = scanner.nextLine();
                    Type type = promptEnum(scanner, Type.class, "Select Type");
                    Wood backWood = promptEnum(scanner, Wood.class, "Select Back Wood");
                    Wood topWood = promptEnum(scanner, Wood.class, "Select Top Wood");
                    System.out.print("Enter Number of Strings: ");
                    int strings = Integer.parseInt(scanner.nextLine());

                    inventory.addGuitar(serial, price, builder, model, type, backWood, topWood, strings);
                    System.out.println(" Guitar added successfully!");
                }
                case 2 -> viewAllGuitars(inventory);
                case 3 -> { return; }
            }
        }
    }

    private static void customerMenu(Scanner scanner, Inventory inventory) {
        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View All Guitars");
            System.out.println("2. Filter Guitars");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");
            int custChoice = getIntInput(scanner, 1, 3);

            switch (custChoice) {
                case 1 -> viewAllGuitars(inventory);
                case 2 -> filterGuitars(scanner, inventory);
                case 3 -> { return; }
            }
        }
    }

    private static void viewAllGuitars(Inventory inventory) {
        List<Guitar> allGuitars = inventory.search(new GuitarSpec(null, null, null, null, null, 0), null, null);
        if (allGuitars.isEmpty()) {
            System.out.println(" No guitars available.");
        } else {
            System.out.println(" Available Guitars:");
            for (Guitar g : allGuitars) {
                GuitarSpec spec = g.getSpec();
                System.out.printf("Model: %-10s | Builder: %-8s | Price: ₹%.2f | Strings: %d | Type: %-8s | Back Wood: %-15s | Top Wood: %-15s%n",
                        spec.getModel(), spec.getBuilder(), g.getPrice(), spec.getNumStrings(),
                        spec.getType(), spec.getBackWood(), spec.getTopWood());
            }
        }
    }

    private static void filterGuitars(Scanner scanner, Inventory inventory) {
        System.out.print("Enter min price (or 0 to skip): ");
        double minPrice = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter max price (or 0 to skip): ");
        double maxPrice = Double.parseDouble(scanner.nextLine());

        System.out.print("Filter by purpose? (beginner/intermediate/advanced/all): ");
        String level = scanner.nextLine().toLowerCase();
        int strings = GuitarHelper.getStringCountByLevel(level);

        System.out.print("Filter by Builder? (y/n): ");
        Builder builder = null;
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            builder = promptEnum(scanner, Builder.class, "Select Builder");
        }

        System.out.print("Filter by Wood? (y/n): ");
        Wood wood = null;
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            wood = promptEnum(scanner, Wood.class, "Select Top Wood");
        }

        GuitarSpec searchSpec = new GuitarSpec(builder, null, null, wood, wood, strings);
        List<Guitar> results = inventory.search(searchSpec,
                minPrice == 0 ? null : minPrice,
                maxPrice == 0 ? null : maxPrice);

        if (results.isEmpty()) {
            System.out.println(" No matching guitars found.");
        } else {
            System.out.println(" Matching Guitars:");
            for (Guitar g : results) {
                GuitarSpec spec = g.getSpec();
                System.out.printf("Model: %-10s | Builder: %-8s | Price: ₹%.2f | Strings: %d | Type: %-8s | Back Wood: %-15s | Top Wood: %-15s%n",
                        spec.getModel(), spec.getBuilder(), g.getPrice(), spec.getNumStrings(),
                        spec.getType(), spec.getBackWood(), spec.getTopWood());
            }

            System.out.print("Would you like to purchase a guitar? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                System.out.println("Purchase successful! Thank you for choosing us.");
            }
        }
    }

    private static void preloadGuitars(Inventory inventory) {
        inventory.addGuitar("SN001", 12000.0, Builder.FENDER, "Stratocaster", Type.ELECTRIC,
                Wood.ALDER, Wood.ALDER, 6);

        inventory.addGuitar("SN002", 15000.0, Builder.MARTIN, "D-28", Type.ACOUSTIC,
                Wood.INDIAN_ROSEWOOD, Wood.SITKA, 6);

        inventory.addGuitar("SN003", 18000.0, Builder.GIBSON, "Les Paul", Type.ELECTRIC,
                Wood.MAPLE, Wood.MAPLE, 12);

        inventory.addGuitar("SN004", 9500.0, Builder.YAMAHA, "F280", Type.ACOUSTIC,
                Wood.MAHOGANY, Wood.SPRUCE, 6);

        inventory.addGuitar("SN005", 22000.0, Builder.OLSON, "SJ", Type.ACOUSTIC,
                Wood.COCOBOLO, Wood.CEDAR, 18);

        System.out.println(" Preloaded guitars added to inventory.");
    }

    private static <T extends Enum<T>> T promptEnum(Scanner scanner, Class<T> enumClass, String prompt) {
        T[] values = enumClass.getEnumConstants();
        while (true) {
            System.out.println("\n" + prompt + ":");
            for (int i = 0; i < values.length; i++) {
                System.out.println((i + 1) + ". " + values[i]);
            }
            try {
                System.out.print("Enter choice number: ");
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= values.length) {
                    return values[choice - 1];
                }
                System.out.println("Invalid choice. Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private static int getIntInput(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) return input;
                else System.out.print("Enter a number between " + min + " and " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}
