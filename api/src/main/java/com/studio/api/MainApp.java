package com.studio.api;

import com.studio.core.Equipment;
import com.studio.parser.InventoryParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class MainApp {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        logger.info("Booting Studio Inventory Manager API...");

        // Sample JSON string to process
        String jsonDatabase = "[\n" +
                "  {\"id\":\"MIC-01\", \"name\":\"Dual Lavalier Collar Microphone\", \"category\":\"Audio\"},\n" +
                "  {\"id\":\"MNT-01\", \"name\":\"Aluminum Tripod\", \"category\":\"Mounts\"},\n" +
                "  {\"id\":\"VIS-01\", \"name\":\"Professional Green Screen Backdrop\", \"category\":\"Visuals\"},\n" +
                "  {\"id\":\"MNT-02\", \"name\":\"Adjustable Microphone Stand with Boom Arm\", \"category\":\"Mounts\"},\n" +
                "  {\"id\":\"VIS-02\", \"name\":\"Backdrop Rod\", \"category\":\"Visuals\"}\n" +
                "]";

        InventoryParser parser = new InventoryParser();
        List<Equipment> inventory = parser.parseInventory(jsonDatabase);

        System.out.println("\n=== CURRENT STUDIO INVENTORY ===");
        for (Equipment item : inventory) {
            System.out.println(item.toString());
        }
        System.out.println("================================\n");

        logger.info("System shutdown successful.");
    }
}