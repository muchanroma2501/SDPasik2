package com.aitu.sdp.assignment2;

/** AMD product in the AMD component family. */
public final class AMDCPU implements CPU {
    private final String model;
    private final int tdpWatts;

    public AMDCPU() {
        this("AMD Ryzen 5 5600G", 65);
    }

    public AMDCPU(String model) {
        this(model, defaultTdp(model));
    }

    public AMDCPU(String model, int tdpWatts) {
        this.model = requireModel(model);
        this.tdpWatts = requireTdp(tdpWatts);
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public int getTdpWatts() {
        return tdpWatts;
    }

    @Override
    public String getSocket() {
        return "AM5";
    }

    private static int defaultTdp(String model) {
        return switch (model) {
            case "AMD Ryzen 5 7600" -> 65;
            case "AMD Ryzen 7 7800X3D" -> 120;
            default -> 170;
        };
    }

    private static String requireModel(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CPU model cannot be null or blank");
        }
        return value.trim();
    }

    private static int requireTdp(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("CPU TDP must be positive");
        }
        return value;
    }
}
