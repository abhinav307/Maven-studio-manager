package com.studio.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.studio.core.Equipment; // Imported securely from our core module!
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;

public class InventoryParser {
    private static final Logger logger = LoggerFactory.getLogger(InventoryParser.class);
    private final Gson gson;

    public InventoryParser() {
        this.gson = new Gson();
    }

    public List<Equipment> parseInventory(String jsonArray) {
        logger.info("Initializing inventory parsing sequence...");
        Type equipmentListType = new TypeToken<List<Equipment>>(){}.getType();
        
        try {
            List<Equipment> equipmentList = gson.fromJson(jsonArray, equipmentListType);
            logger.info("Successfully loaded {} pieces of equipment into the system.", equipmentList.size());
            return equipmentList;
        } catch (Exception e) {
            logger.error("Failed to parse inventory: {}", e.getMessage());
            throw new RuntimeException("Inventory load failed", e);
        }
    }
}