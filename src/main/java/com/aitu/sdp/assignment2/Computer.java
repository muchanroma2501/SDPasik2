package com.aitu.sdp.assignment2;

/**
 * Immutable product assembled by the Builder.
 *
 * <p>Clean Code principle: Immutability and Encapsulation. Every field is
 * private and final, and construction validates all required values.</p>
 */
public final class Computer {
    private final String cpu;
    private final String gpu;
    private final String ram;
    private final String storage;
    private final String powerSupply;
    private final String motherboard;
    private final String coolingSystem;
    private final boolean hasRGB;

    public Computer(String cpu, String gpu, String ram, String storage,
                    String powerSupply, String motherboard,
                    String coolingSystem, boolean hasRGB) {
        this.cpu = requireText(cpu, "CPU");
        this.gpu = requireText(gpu, "GPU");
        this.ram = requireText(ram, "RAM");
        this.storage = requireText(storage, "Storage");
        this.powerSupply = requireText(powerSupply, "Power supply");
        this.motherboard = requireText(motherboard, "Motherboard");
        this.coolingSystem = requireText(coolingSystem, "Cooling system");
        this.hasRGB = hasRGB;
    }

    private static String requireText(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " cannot be null or blank");
        }
        return value.trim();
    }

    public String getCpu() { return cpu; }
    public String getGpu() { return gpu; }
    public String getRam() { return ram; }
    public String getStorage() { return storage; }
    public String getPowerSupply() { return powerSupply; }
    public String getMotherboard() { return motherboard; }
    public String getCoolingSystem() { return coolingSystem; }
    public boolean isHasRGB() { return hasRGB; }

    @Override
    public String toString() {
        return "Computer {" + System.lineSeparator()
                + "  CPU: " + cpu + System.lineSeparator()
                + "  GPU: " + gpu + System.lineSeparator()
                + "  RAM: " + ram + System.lineSeparator()
                + "  Storage: " + storage + System.lineSeparator()
                + "  Power Supply: " + powerSupply + System.lineSeparator()
                + "  Motherboard: " + motherboard + System.lineSeparator()
                + "  Cooling System: " + coolingSystem + System.lineSeparator()
                + "  RGB Lighting: " + (hasRGB ? "Enabled" : "Disabled") + System.lineSeparator()
                + "}";
    }
}
