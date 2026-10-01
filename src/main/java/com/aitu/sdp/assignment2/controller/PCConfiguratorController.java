package com.aitu.sdp.assignment2.controller;

import com.aitu.sdp.assignment2.AMDComponentFactory;
import com.aitu.sdp.assignment2.BudgetPCOrderFactory;
import com.aitu.sdp.assignment2.ComponentFactory;
import com.aitu.sdp.assignment2.Computer;
import com.aitu.sdp.assignment2.ComputerBuilder;
import com.aitu.sdp.assignment2.ComputerOrderFactory;
import com.aitu.sdp.assignment2.GamingComputerBuilder;
import com.aitu.sdp.assignment2.GamingPCOrderFactory;
import com.aitu.sdp.assignment2.IntelNvidiaComponentFactory;
import com.aitu.sdp.assignment2.Storage;
import com.aitu.sdp.assignment2.WorkstationPCOrderFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RestController
@RequestMapping("/api/pc")
@CrossOrigin
public class PCConfiguratorController {
    private static final Map<String, ComponentFactory> COMPONENT_FACTORIES = Map.of(
            "intel_nvidia", new IntelNvidiaComponentFactory(),
            "amd", new AMDComponentFactory());
    private static final Map<String, Function<ComponentFactory, ComputerOrderFactory>> ORDER_FACTORIES = Map.of(
            "gaming", GamingPCOrderFactory::new,
            "budget", BudgetPCOrderFactory::new,
            "workstation", WorkstationPCOrderFactory::new);

    @GetMapping("/presets")
    public List<PCResponse> presets() {
        ComponentFactory factory = new IntelNvidiaComponentFactory();
        return List.of(
                response("Ultimate Gaming Beast",
                        new GamingPCOrderFactory(factory).orderComputer(), "gaming"),
                response("Budget Office PC",
                        new BudgetPCOrderFactory(factory).orderComputer(), "budget"),
                response("Creator Workstation",
                        new WorkstationPCOrderFactory(factory).orderComputer(), "workstation"));
    }

    @GetMapping("/catalog")
    public Map<String, Object> catalog(
            @RequestParam(name = "ecosystem", defaultValue = "intel_nvidia")
            String ecosystem) {
        return new PerformanceCatalog(createComponentFactory(ecosystem).family()).values();
    }

    @PostMapping("/custom")
    public PCResponse custom(@RequestBody CustomPCRequest request) {
        validateCustom(request);
        ComponentFactory factory = createComponentFactory(request.getEcosystem());
        Computer computer;
        try {
            ComputerBuilder builder = new GamingComputerBuilder(factory);
            computer = builder
                    .cpu(factory.createCPU(request.getCpu().trim()))
                    .gpu(factory.createGPU(request.getGpu().trim()))
                    .motherboard(factory.createMotherboard(request.getMotherboard().trim()))
                    .ram(request.getRam().trim())
                    .storage(new Storage(request.getStorage().trim()))
                    .powerSupply(factory.createPowerSupply(request.getPowerSupply().trim()))
                    .withRGB(request.isHasRGB())
                    .build();
        } catch (IllegalArgumentException exception) {
            throw invalid(exception.getMessage());
        }
        return response("Custom Studio Build", computer, "custom");
    }

    /** Compatibility endpoint retained for existing clients. */
    @PostMapping("/build")
    public PCResponse build(@RequestBody BuildPCRequest request) {
        validateBuild(request);
        ComponentFactory factory = createComponentFactory(request.getEcosystem());
        ComputerOrderFactory orderFactory = createOrderFactory(request.getBuildType(), factory);
        return response(request.getBuildType() + " build",
                orderFactory.orderComputer(request.getRam().trim(),
                        request.getStorage().trim(), request.isHasRGB()),
                request.getBuildType());
    }

    private PCResponse response(String name, Computer computer, String pipeline) {
        Map<String, String> specs = new LinkedHashMap<>();
        specs.put("CPU", computer.getCpu());
        specs.put("GPU", computer.getGpu());
        specs.put("RAM", computer.getRam());
        specs.put("Storage", computer.getStorage());
        specs.put("Power Supply", computer.getPowerSupply());
        specs.put("Motherboard", computer.getMotherboard());
        specs.put("Cooling System", computer.getCoolingSystem());
        specs.put("RGB Lighting", computer.isHasRGB() ? "Enabled" : "Disabled");
        int required = computer.getCpuComponent().getTdpWatts()
                + computer.getGpuComponent().getTdpWatts() + 150;
        String highlights = "Abstract Factory selected the CPU/GPU family. Factory Method "
                + "executed " + pipeline + " order pipeline. Builder validated the PSU "
                + "and created an immutable Computer instance.";
        return new PCResponse(specs, "Valid - compatible configuration.", highlights, name, required);
    }

    private ComponentFactory createComponentFactory(String ecosystem) {
        ComponentFactory factory = COMPONENT_FACTORIES.get(ecosystem);
        if (factory == null) {
            throw invalid("ecosystem must be 'intel_nvidia' or 'amd'");
        }
        return factory;
    }

    private ComputerOrderFactory createOrderFactory(String type, ComponentFactory factory) {
        Function<ComponentFactory, ComputerOrderFactory> creator = ORDER_FACTORIES.get(type);
        if (creator == null) {
            throw invalid("buildType must be gaming, budget, or workstation");
        }
        return creator.apply(factory);
    }

    private void validateCustom(CustomPCRequest request) {
        if (request == null) throw invalid("Request body is required");
        require(request.getEcosystem(), "ecosystem");
        require(request.getCpu(), "cpu");
        require(request.getGpu(), "gpu");
        require(request.getMotherboard(), "motherboard");
        require(request.getRam(), "ram");
        require(request.getStorage(), "storage");
        require(request.getPowerSupply(), "powerSupply");
    }

    private void validateBuild(BuildPCRequest request) {
        if (request == null) throw invalid("Request body is required");
        require(request.getEcosystem(), "ecosystem");
        require(request.getBuildType(), "buildType");
        require(request.getRam(), "ram");
        require(request.getStorage(), "storage");
    }

    private void require(String value, String field) {
        if (value == null || value.isBlank()) throw invalid(field + " cannot be blank");
    }

    private ResponseStatusException invalid(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
