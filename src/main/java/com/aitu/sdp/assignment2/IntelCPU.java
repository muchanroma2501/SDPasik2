package com.aitu.sdp.assignment2;

/** Intel product in the Intel/NVIDIA component family. */
public final class IntelCPU implements CPU {
    private final String model;
    private final int tdpWatts;

    public IntelCPU() {
        this("Intel Core i9-14900K", 253);
    }

    public IntelCPU(String model) {
        this(model, defaultTdp(model));
    }

    public IntelCPU(String model, int tdpWatts) {
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
        return "LGA1700";
    }

    private static int defaultTdp(String model) {
        return switch (model) {
            case "Intel Core i5-13400" -> 65;
            case "Intel Core i7-14700K" -> 205;
            default -> 253;
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
