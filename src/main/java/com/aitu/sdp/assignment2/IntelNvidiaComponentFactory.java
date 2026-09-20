package com.aitu.sdp.assignment2;

/** Creates compatible Intel and NVIDIA components. */
public final class IntelNvidiaComponentFactory implements ComponentFactory {
    @Override
    public CPU createCPU(PerformanceTier tier) {
        return tier == PerformanceTier.HIGH
                ? new IntelCPU("Intel Core i9-14900K")
                : new IntelCPU("Intel Core i5-13400");
    }

    @Override
    public GPU createGPU(PerformanceTier tier) {
        return tier == PerformanceTier.HIGH
                ? new NvidiaGPU("NVIDIA GeForce RTX 4090 24GB")
                : new NvidiaGPU("NVIDIA GeForce RTX 3050 8GB");
    }

    @Override
    public CPU createCPU(String model) {
        return new IntelCPU(model);
    }

    @Override
    public GPU createGPU(String model) {
        return new NvidiaGPU(model);
    }

    @Override
    public Motherboard createMotherboard(PerformanceTier tier) {
        return tier == PerformanceTier.HIGH
                ? new Z790_ATX_Motherboard() : new B760_MicroATX_Motherboard();
    }

    @Override
    public Motherboard createMotherboard(String model) {
        return switch (model) {
            case "Z790 ATX" -> new Z790_ATX_Motherboard();
            case "B760 Micro-ATX" -> new B760_MicroATX_Motherboard();
            default -> throw new IllegalArgumentException("Unknown Intel motherboard: " + model);
        };
    }

    @Override
    public String storage(PerformanceTier tier) {
        return tier == PerformanceTier.HIGH ? "2TB NVMe Gen4 SSD" : "512GB NVMe SSD";
    }

    @Override
    public String powerSupply(PerformanceTier tier) {
        return tier == PerformanceTier.HIGH ? "1000W 80+ Gold" : "500W 80+ Bronze";
    }

    @Override
    public String coolingSystem(PerformanceTier tier) {
        return tier == PerformanceTier.HIGH ? "360mm AIO Liquid Cooling" : "Stock Air Cooler";
    }
}
