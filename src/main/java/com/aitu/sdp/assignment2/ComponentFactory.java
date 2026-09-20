package com.aitu.sdp.assignment2;

/** Abstract Factory for matching CPU and GPU product families. */
public interface ComponentFactory {
    CPU createCPU(PerformanceTier tier);

    GPU createGPU(PerformanceTier tier);

    CPU createCPU(String model);

    GPU createGPU(String model);

    Motherboard createMotherboard(PerformanceTier tier);

    Motherboard createMotherboard(String model);

    String storage(PerformanceTier tier);

    String powerSupply(PerformanceTier tier);

    String coolingSystem(PerformanceTier tier);

}
