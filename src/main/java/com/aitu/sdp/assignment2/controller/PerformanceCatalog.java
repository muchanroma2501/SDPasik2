package com.aitu.sdp.assignment2.controller;

import com.aitu.sdp.assignment2.ComponentFactory;
import com.aitu.sdp.assignment2.CPU;
import com.aitu.sdp.assignment2.GPU;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Catalog projection created by the selected Abstract Factory family. */
final class PerformanceCatalog {
    private final ComponentFactory factory;
    private final String ecosystem;

    PerformanceCatalog(ComponentFactory factory, String ecosystem) {
        this.factory = factory;
        this.ecosystem = ecosystem;
    }

    Map<String, Object> values() {
        List<CPU> cpus = ecosystem.equals("amd")
                ? List.of(factory.createCPU("AMD Ryzen 5 7600"),
                factory.createCPU("AMD Ryzen 7 7800X3D"),
                factory.createCPU("AMD Ryzen 9 7950X"))
                : List.of(factory.createCPU("Intel Core i5-13400"),
                factory.createCPU("Intel Core i7-14700K"),
                factory.createCPU("Intel Core i9-14900K"));
        List<GPU> gpus = ecosystem.equals("amd")
                ? List.of(factory.createGPU("Radeon Vega Integrated Graphics"),
                factory.createGPU("AMD Radeon RX 6600 8GB"),
                factory.createGPU("AMD Radeon RX 7700 XT 12GB"),
                factory.createGPU("AMD Radeon RX 7900 XT 20GB"),
                factory.createGPU("AMD Radeon RX 7900 XTX 24GB"))
                : List.of(factory.createGPU("NVIDIA GeForce RTX 3050 8GB"),
                factory.createGPU("NVIDIA GeForce RTX 4070 Ti 12GB"),
                factory.createGPU("NVIDIA GeForce RTX 4080 Super 16GB"),
                factory.createGPU("NVIDIA GeForce RTX 4090 24GB"));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("cpus", cpus.stream().map(c -> Map.of("model", c.getModel(),
                "tdpWatts", c.getTdpWatts(), "socket", c.getSocket())).toList());
        result.put("gpus", gpus.stream().map(g -> Map.of("model", g.getModel(),
                "tdpWatts", g.getTdpWatts())).toList());
        result.put("motherboards", ecosystem.equals("amd")
                ? List.of(Map.of("model", "B650 ATX", "socket", "AM5"),
                Map.of("model", "X670E ATX", "socket", "AM5"))
                : List.of(Map.of("model", "B760 Micro-ATX", "socket", "LGA1700"),
                Map.of("model", "Z790 ATX", "socket", "LGA1700")));
        result.put("ram", List.of("16GB DDR4", "32GB DDR5", "64GB DDR5"));
        result.put("storage", List.of("512GB NVMe SSD", "1TB NVMe SSD", "2TB NVMe Gen4 SSD"));
        result.put("powerSupplies", List.of("500W 80+ Bronze", "650W 80+ Gold",
                "850W 80+ Gold", "1000W 80+ Gold"));
        return result;
    }
}
