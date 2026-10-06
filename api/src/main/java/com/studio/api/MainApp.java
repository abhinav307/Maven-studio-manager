package com.studio.api;

import com.studio.core.Equipment;
import com.studio.parser.InventoryParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class MainApp {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        logger.info("Booting Studio Inventory Manager API...");

        // Sample JSON string to process (Database)
        String jsonDatabase = "[\n" +
                "  {\"id\":\"MIC-01\", \"name\":\"Dual Lavalier Collar Microphone\", \"category\":\"Audio\"},\n" +
                "  {\"id\":\"MNT-01\", \"name\":\"Aluminum Tripod\", \"category\":\"Mounts\"},\n" +
                "  {\"id\":\"VIS-01\", \"name\":\"Professional Green Screen Backdrop\", \"category\":\"Visuals\"},\n" +
                "  {\"id\":\"MNT-02\", \"name\":\"Adjustable Microphone Stand with Boom Arm\", \"category\":\"Mounts\"},\n" +
                "  {\"id\":\"VIS-02\", \"name\":\"Backdrop Rod\", \"category\":\"Visuals\"}\n" +
                "]";

        InventoryParser parser = new InventoryParser();
        List<Equipment> inventory = parser.parseInventory(jsonDatabase);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=========================================");
        System.out.println("  WELCOME TO STUDIO INVENTORY MANAGER   ");
        System.out.println("=========================================\n");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. View Full Inventory");
            System.out.println("2. View Available (Unassigned) Equipment");
            System.out.println("3. Assign / Checkout Equipment");
            System.out.println("4. Return Equipment");
            System.out.println("5. Exit");
            System.out.print("Select an option (1-5): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    printInventory(inventory, null);
                    break;
                case "2":
                    printInventory(inventory, false);
                    break;
                case "3":
                    handleAssignment(inventory, scanner, true);
                    break;
                case "4":
                    handleAssignment(inventory, scanner, false);
                    break;
                case "5":
                    running = false;
                    System.out.println("Shutting down the manager. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
        logger.info("System shutdown successful.");
    }

    private static void printInventory(List<Equipment> inventory, Boolean assignmentFilter) {
        System.out.println("\n=== STUDIO INVENTORY ===");
        boolean found = false;
        for (Equipment item : inventory) {
            if (assignmentFilter == null || item.isAssigned() == assignmentFilter) {
                System.out.println(item.toString());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No items match this criteria.");
        }
        System.out.println("========================\n");
    }

    private static void handleAssignment(List<Equipment> inventory, Scanner scanner, boolean assignAction) {
        String actionName = assignAction ? "Checkout" : "Return";
        System.out.print("\nEnter the ID of the equipment to " + actionName + " (e.g. MIC-01): ");
        String targetId = scanner.nextLine().trim().toUpperCase();

        boolean found = false;
        for (Equipment item : inventory) {
            if (item.getId().equalsIgnoreCase(targetId)) {
                found = true;
                if (item.isAssigned() == assignAction) {
                    System.out.println("ERROR: " + item.getName() + " is already " + (assignAction ? "checked out!" : "returned!"));
                } else {
                    item.setAssigned(assignAction);
                    System.out.println("SUCCESS: " + item.getName() + " has been successfully " + (assignAction ? "checked out." : "returned."));
                }
                break;
            }
        }

        if (!found) {
            System.out.println("ERROR: Could not find any equipment with ID: " + targetId);
        }
    }
}