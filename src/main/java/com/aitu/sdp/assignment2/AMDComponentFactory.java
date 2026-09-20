package com.aitu.sdp.assignment2;

/** Creates compatible AMD components. */
public final class AMDComponentFactory implements ComponentFactory {
    @Override
    public CPU createCPU(PerformanceTier tier) {
        return tier == PerformanceTier.HIGH
                ? new AMDCPU("AMD Ryzen 7 7800X3D")
                : new AMDCPU("AMD Ryzen 5 5600G");
    }

    @Override
    public GPU createGPU(PerformanceTier tier) {
        return tier == PerformanceTier.HIGH
                ? new AMDGPU("AMD Radeon RX 7900 XT 20GB")
                : new AMDGPU("Radeon Vega Integrated Graphics");
    }

    @Override
    public CPU createCPU(String model) {
        return new AMDCPU(model);
    }

    @Override
    public GPU createGPU(String model) {
        return new AMDGPU(model);
    }

    @Override
    public Motherboard createMotherboard(PerformanceTier tier) {
        return tier == PerformanceTier.HIGH
                ? new X670E_ATX_Motherboard() : new B650_ATX_Motherboard();
    }

    @Override
    public Motherboard createMotherboard(String model) {
        return switch (model) {
            case "X670E ATX" -> new X670E_ATX_Motherboard();
            case "B650 ATX" -> new B650_ATX_Motherboard();
            default -> throw new IllegalArgumentException("Unknown AMD motherboard: " + model);
        };
    }

    @Override
    public String storage(PerformanceTier tier) {
        return tier == PerformanceTier.HIGH ? "2TB NVMe Gen4 SSD" : "512GB NVMe SSD";
    }

    @Override
    public String powerSupply(PerformanceTier tier) {
        return tier == PerformanceTier.HIGH ? "850W 80+ Gold" : "500W 80+ Bronze";
    }

    @Override
    public String coolingSystem(PerformanceTier tier) {
        return tier == PerformanceTier.HIGH
                ? "280mm AIO Liquid Cooling"
                : "Stock AMD Wraith Stealth";
    }
}
