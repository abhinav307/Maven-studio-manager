package com.studio.core;

public class Equipment {
    private String id;
    private String name;
    private String category;
    private boolean isAssigned;

    public Equipment() {}

    public Equipment(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.isAssigned = false;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public boolean isAssigned() { return isAssigned; }
    
    public void setAssigned(boolean assigned) { this.isAssigned = assigned; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - Assigned: %b", id, name, category, isAssigned);
    }
}