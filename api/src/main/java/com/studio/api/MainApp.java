package com.studio.api;

import com.google.gson.Gson;
import com.studio.core.Equipment;
import com.studio.parser.InventoryParser;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MainApp {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);
    private static List<Equipment> inventory;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        logger.info("Booting Studio Inventory Manager Web API...");

        // Initial database load
        String jsonDatabase = "[\n" +
                "  {\"id\":\"MIC-01\", \"name\":\"Dual Lavalier Collar Microphone\", \"category\":\"Audio\"},\n" +
                "  {\"id\":\"MNT-01\", \"name\":\"Aluminum Tripod\", \"category\":\"Mounts\"},\n" +
                "  {\"id\":\"VIS-01\", \"name\":\"Professional Green Screen Backdrop\", \"category\":\"Visuals\"},\n" +
                "  {\"id\":\"MNT-02\", \"name\":\"Adjustable Microphone Stand with Boom Arm\", \"category\":\"Mounts\"},\n" +
                "  {\"id\":\"VIS-02\", \"name\":\"Backdrop Rod\", \"category\":\"Visuals\"}\n" +
                "]";

        InventoryParser parser = new InventoryParser();
        inventory = parser.parseInventory(jsonDatabase);

        // Start Web Server
        Javalin app = Javalin.create(config -> {
            // Serve our static HTML file
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(8080);

        // API Endpoint: Get all inventory
        app.get("/api/inventory", ctx -> {
            ctx.result(gson.toJson(inventory)).contentType("application/json");
        });

        // API Endpoint: Toggle Assignment Status
        app.post("/api/toggle", ctx -> {
            String targetId = ctx.queryParam("id");
            for (Equipment item : inventory) {
                if (item.getId().equalsIgnoreCase(targetId)) {
                    item.setAssigned(!item.isAssigned());
                    ctx.result("Success");
                    return;
                }
            }
            ctx.status(404).result("Item not found");
        });
        
        // API Endpoint: Add Custom Equipment
        app.post("/api/add", ctx -> {
            Equipment newItem = gson.fromJson(ctx.body(), Equipment.class);
            
            // Check if ID already exists to prevent duplicates
            for (Equipment item : inventory) {
                if (item.getId().equalsIgnoreCase(newItem.getId())) {
                    ctx.status(400).result("Error: Equipment with this ID already exists!");
                    return;
                }
            }
            
            inventory.add(newItem);
            ctx.result("Successfully added!");
        });
        
        System.out.println("=========================================");
        System.out.println("  WEB SERVER RUNNING AT: http://localhost:8080 ");
        System.out.println("=========================================");
    }
}