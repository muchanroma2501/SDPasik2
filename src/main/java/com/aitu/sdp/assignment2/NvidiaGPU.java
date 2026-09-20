package com.aitu.sdp.assignment2;

/** NVIDIA product in the Intel/NVIDIA component family. */
public final class NvidiaGPU implements GPU {
    private final String model;
    private final int tdpWatts;

    public NvidiaGPU() {
        this("NVIDIA GeForce RTX 4090 24GB", 450);
    }

    public NvidiaGPU(String model) {
        this(model, defaultTdp(model));
    }

    public NvidiaGPU(String model, int tdpWatts) {
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

    private static int defaultTdp(String model) {
        return switch (model) {
            case "NVIDIA GeForce RTX 3050 8GB" -> 130;
            case "NVIDIA GeForce RTX 4080 Super 16GB" -> 320;
            case "NVIDIA GeForce RTX 4070 Ti 12GB" -> 285;
            default -> 450;
        };
    }

    private static String requireModel(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("GPU model cannot be null or blank");
        }
        return value.trim();
    }

    private static int requireTdp(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("GPU TDP must be positive");
        }
        return value;
    }
}
