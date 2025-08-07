package com.bp.test;

import com.fo.dao.*;
import com.fo.model.*;
import java.util.*;

public class MainApp {
    static Scanner sc = new Scanner(System.in);
    static FoodItemDao foodItemDao = new FoodItemDao();
    static OrderDao orderDao = new OrderDao();
    static DiscountDao discountDao = new DiscountDao();
    static DeliveryAgentDao deliveryAgentDao = new DeliveryAgentDao();
    static UserDao userDao = new UserDao();

    public static void main(String[] args) {
    	 while (true) {
    	        System.out.println("\n--- Welcome ---");
    	        System.out.print("Username: ");
    	        String user = sc.next();
    	        System.out.print("Password: ");
    	        String pass = sc.next();

    	        String role = userDao.authenticate(user, pass);
    	        if (role == null) {
    	            System.out.println("Invalid credentials!\n");
    	            continue;
    	        }

    	        if (role.equals("admin")) {
    	            System.out.println("\nLogin successful as Admin.");
    	            adminMenu();
    	        } else if (role.equals("customer")) {
    	            System.out.println("\nLogin successful as Customer.");
    	            customerMenu(user);  // pass username
    	        }
    	    }
    	}

    static void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. Add Food Item");
            System.out.println("2. View Menu");
            System.out.println("3. Add Delivery Agent");
            System.out.println("4. View Delivery Agents");
            System.out.println("5. Update Discount Rule");
            System.out.println("6. Back");

            int ch = sc.nextInt();
            switch (ch) {
                case 1 -> {
                    sc.nextLine();
                    System.out.print("Enter food name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter price: ");
                    double price = sc.nextDouble();
                    foodItemDao.addItem(name, price);
                }
                case 2 -> displayMenu();
                case 3 -> {
                    sc.nextLine();
                    System.out.print("Enter delivery agent name: ");
                    String name = sc.nextLine();
                    deliveryAgentDao.addAgent(name);
                }
                case 4 -> {
                    List<String> agents = deliveryAgentDao.getAllAgents();
                    System.out.println("--- Delivery Agents ---");
                    agents.forEach(System.out::println);
                }
                case 5 -> {
                    System.out.print("Enter minimum order amount for discount: ");
                    double min = sc.nextDouble();
                    System.out.print("Enter discount amount: ");
                    double disc = sc.nextDouble();
                    discountDao.updateDiscountRule(min, disc);
                }
                case 6 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    static void displayMenu() {
        List<FoodItem> menu = foodItemDao.getAllItems();
        System.out.println("\n--- MENU ---");
        for (FoodItem item : menu) {
            System.out.println(item);
        }
    }

    static void customerMenu(String customerName) {
        sc.nextLine();
        String customer = customerName;

        while (true) {
            System.out.println("\n--- Customer Panel ---");
            System.out.println("1. View Menu & Place Order");
            System.out.println("2. Back");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    List<FoodItem> menu = foodItemDao.getAllItems();
                    Map<FoodItem, Integer> orderItems = new LinkedHashMap<>();

                    while (true) {
                        displayMenu();
                        System.out.print("Select Item ID (0 to finish): ");
                        if (!sc.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a valid item ID.");
                            sc.next();
                            continue;
                        }
                        int id = sc.nextInt();
                        if (id == 0) break;

                        FoodItem selected = menu.stream().filter(i -> i.getId() == id).findFirst().orElse(null);
                        if (selected == null) {
                            System.out.println("Invalid item.");
                            continue;
                        }

                        System.out.print("Enter quantity: ");
                        if (!sc.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a valid quantity.");
                            sc.next();
                            continue;
                        }
                        int qty = sc.nextInt();
                        orderItems.put(selected, orderItems.getOrDefault(selected, 0) + qty);
                    }

                    if (orderItems.isEmpty()) {
                        System.out.println("No items selected. Returning to menu.");
                        break;
                    }

                    double total = 0;
                    StringBuilder itemDetails = new StringBuilder();
                    for (Map.Entry<FoodItem, Integer> entry : orderItems.entrySet()) {
                        FoodItem f = entry.getKey();
                        int qty = entry.getValue();
                        double subTotal = f.getPrice() * qty;
                        total += subTotal;
                        itemDetails.append(f.getName()).append(" x").append(qty).append(" = ₹").append(subTotal).append("\n");
                    }

                    double discount = discountDao.getDiscountAmount(total);
                    double finalAmount = total - discount;

                    System.out.print("Payment Mode (Cash/UPI): ");
                    sc.nextLine(); // consume newline
                    String mode = sc.nextLine();

                    List<String> partners = deliveryAgentDao.getAllAgents();
                    String partner = partners.isEmpty() ? "Not Assigned" :
                        partners.get(new Random().nextInt(partners.size()));

                    Order order = new Order(customer, itemDetails.toString(), total, discount, mode, partner, finalAmount);
                    orderDao.saveOrder(order);
                    System.out.println(order.getInvoice());
                    break;

                case 2:
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
}
}
