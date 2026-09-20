package com.aitu.sdp.assignment2.controller;

import com.aitu.sdp.assignment2.AMDComponentFactory;
import com.aitu.sdp.assignment2.BudgetPCOrderFactory;
import com.aitu.sdp.assignment2.ComponentFactory;
import com.aitu.sdp.assignment2.CPU;
import com.aitu.sdp.assignment2.Computer;
import com.aitu.sdp.assignment2.ComputerBuilder;
import com.aitu.sdp.assignment2.ComputerOrderFactory;
import com.aitu.sdp.assignment2.GPU;
import com.aitu.sdp.assignment2.GamingPCOrderFactory;
import com.aitu.sdp.assignment2.IntelNvidiaComponentFactory;
import com.aitu.sdp.assignment2.Motherboard;
import com.aitu.sdp.assignment2.PowerSupply;
import com.aitu.sdp.assignment2.WorkstationPCOrderFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pc")
@CrossOrigin
public class PCConfiguratorController {

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
            @org.springframework.web.bind.annotation.RequestParam(name = "ecosystem",
                    defaultValue = "intel_nvidia")
            String ecosystem) {
        return new PerformanceCatalog(createComponentFactory(ecosystem), ecosystem).values();
    }

    @PostMapping("/custom")
    public PCResponse custom(@RequestBody CustomPCRequest request) {
        validateCustom(request);
        ComponentFactory factory = createComponentFactory(request.getEcosystem());
        CPU cpu = factory.createCPU(request.getCpu().trim());
        GPU gpu = factory.createGPU(request.getGpu().trim());
        Motherboard motherboard = factory.createMotherboard(request.getMotherboard().trim());
        validateFamily(request.getEcosystem(), cpu, gpu);

        ComputerBuilder builder = new com.aitu.sdp.assignment2.GamingComputerBuilder(factory);
        Computer computer = builder
                .cpu(cpu)
                .gpu(gpu)
                .motherboard(motherboard)
                .ram(request.getRam().trim())
                .storage(request.getStorage().trim())
                .powerSupply(new PowerSupply(request.getPowerSupply().trim(),
                        parseWattage(request.getPowerSupply())))
                .withRGB(request.isHasRGB())
                .build();
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
        int required = tdp(computer.getCpu()) + tdp(computer.getGpu()) + 150;
        String highlights = "Abstract Factory selected the CPU/GPU family. Factory Method "
                + "executed " + pipeline + " order pipeline. Builder validated the PSU "
                + "and created an immutable Computer instance.";
        return new PCResponse(specs, "Valid - compatible configuration.", highlights, name, required);
    }

    private int tdp(String model) {
        if (model.startsWith("Intel")) {
            return new IntelNvidiaComponentFactory().createCPU(model).getTdpWatts();
        }
        if (model.startsWith("NVIDIA")) {
            return new IntelNvidiaComponentFactory().createGPU(model).getTdpWatts();
        }
        if (model.startsWith("AMD Ryzen")) {
            return new AMDComponentFactory().createCPU(model).getTdpWatts();
        }
        return new AMDComponentFactory().createGPU(model).getTdpWatts();
    }

    private void validateFamily(String ecosystem, CPU cpu, GPU gpu) {
        boolean intelFamily = "intel_nvidia".equals(ecosystem);
        boolean valid = intelFamily
                ? cpu.getModel().startsWith("Intel") && gpu.getModel().startsWith("NVIDIA")
                : cpu.getModel().startsWith("AMD Ryzen")
                && (gpu.getModel().startsWith("AMD") || gpu.getModel().startsWith("Radeon"));
        if (!valid) {
            throw invalid("CPU and GPU must belong to the selected ecosystem");
        }
    }

    private ComponentFactory createComponentFactory(String ecosystem) {
        return switch (ecosystem) {
            case "intel_nvidia" -> new IntelNvidiaComponentFactory();
            case "amd" -> new AMDComponentFactory();
            default -> throw invalid("ecosystem must be 'intel_nvidia' or 'amd'");
        };
    }

    private ComputerOrderFactory createOrderFactory(String type, ComponentFactory factory) {
        return switch (type) {
            case "gaming" -> new GamingPCOrderFactory(factory);
            case "budget" -> new BudgetPCOrderFactory(factory);
            case "workstation" -> new WorkstationPCOrderFactory(factory);
            default -> throw invalid("buildType must be gaming, budget, or workstation");
        };
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

    private int parseWattage(String value) {
        try {
            return Integer.parseInt(value.trim().replaceFirst("^([0-9]+).*$", "$1"));
        } catch (NumberFormatException exception) {
            throw invalid("powerSupply must begin with a wattage, for example 850W 80+ Gold");
        }
    }
}
