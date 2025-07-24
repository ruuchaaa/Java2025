package com.ap.GUITAR.model;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CustomerMenu {
    public static void handleCustomer(Scanner scanner, Inventory inventory) {
        System.out.println("\n--- Welcome Customer ---");

        // Step 1: Builder selection
        Builder builder = promptEnumSkip(scanner, Builder.class, "Builder");
        GuitarSpec spec = new GuitarSpec(builder, null, null, null, null, 0);
        List<Guitar> matches = inventory.search(spec);

        if (matches.isEmpty()) {
            System.out.println("❌ No guitars found for selected builder.");
            return;
        }

        System.out.println("🎸 Matching guitars by builder:");
        printGuitarsTable(matches);

        // Step 2: More filtering
        System.out.print("\nFilter further by specifications? (yes/no): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            Type type = promptEnumSkip(scanner, Type.class, "Type");
            Wood backWood = promptEnumSkip(scanner, Wood.class, "BackWood");
            Wood topWood = promptEnumSkip(scanner, Wood.class, "TopWood");

            System.out.print("Enter number of strings (or 0 to skip): ");
            int numStrings = scanner.nextInt();
            scanner.nextLine();

            // Build updated spec
            GuitarSpec spec2 = new GuitarSpec(builder, null, type, backWood, topWood, numStrings);
            matches = inventory.search(spec2);

            if (matches.isEmpty()) {
                System.out.println("❌ No guitars found for those specs.");
                return;
            }

            System.out.println("🎯 Matching guitars after filtering:");
            printGuitarsTable(matches);
        }

        // Step 3: Use-case filtering
        System.out.print("\nFilter by use-case (Beginner/Intermediate/Advanced)? (yes/no): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            System.out.println("1. Beginner (<= ₹10000)");
            System.out.println("2. Intermediate (₹10001 - ₹25000)");
            System.out.println("3. Advanced (> ₹25000)");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            double min = 0, max = Double.MAX_VALUE;
            if (choice == 1) {
                max = 10000;
            } else if (choice == 2) {
                min = 10001;
                max = 25000;
            } else if (choice == 3) {
                min = 25001;
            }

            final double minFinal = min;
            final double maxFinal = max;

            matches = matches.stream()
                    .filter(g -> g.getPrice() >= minFinal && g.getPrice() <= maxFinal)
                    .collect(Collectors.toList());

            if (matches.isEmpty()) {
                System.out.println("❌ No guitars found in this price range.");
                return;
            }

            System.out.println("🎯 Guitars for selected use-case:");
            printGuitarsTable(matches);
        }

        System.out.println("\n✅ Done! You may now choose a guitar from above or consult the seller.");
    }

    // Enums with skip option
    private static <T extends Enum<T>> T promptEnumSkip(Scanner scanner, Class<T> enumClass, String label) {
        T[] values = enumClass.getEnumConstants();
        System.out.println("Select " + label + " (or 0 to skip):");
        for (int i = 0; i < values.length; i++) {
            System.out.println((i + 1) + ". " + values[i].toString().toLowerCase());
        }
        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return (choice == 0) ? null : values[choice - 1];
    }

    private static void printGuitarsTable(List<Guitar> guitars) {
        System.out.println("\n| Serial No                         | Builder | Model | Type     | BackWood         | TopWood          | Strings | Price   |");
        System.out.println("|----------------------------------|---------|-------|----------|------------------|------------------|---------|---------|");
        for (Guitar g : guitars) {
            GuitarSpec s = g.getSpec();
            System.out.printf("| %-32s | %-7s | %-5s | %-8s | %-16s | %-16s | %-7d | ₹%-7.2f |\n",
                    g.getSerialNumber(), s.getBuilder(), s.getModel(), s.getType(),
                    s.getBackWood(), s.getTopWood(), s.getNumStrings(), g.getPrice());
        }
    }
}