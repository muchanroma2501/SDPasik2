package com.aitu.sdp.assignment2;

/** AMD product in the AMD component family. */
public final class AMDGPU implements GPU {
    private final String model;
    private final int tdpWatts;

    public AMDGPU() {
        this("Radeon Vega Integrated Graphics", 35);
    }

    public AMDGPU(String model) {
        this(model, defaultTdp(model));
    }

    public AMDGPU(String model, int tdpWatts) {
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
            case "AMD Radeon RX 7900 XTX 24GB" -> 355;
            case "AMD Radeon RX 7900 XT 20GB" -> 315;
            case "AMD Radeon RX 7700 XT 12GB" -> 245;
            case "AMD Radeon RX 6600 8GB" -> 132;
            default -> 35;
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
