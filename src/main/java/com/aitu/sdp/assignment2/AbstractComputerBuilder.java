package com.aitu.sdp.assignment2;

/**
 * Shared builder implementation.
 *
 * <p>Clean Code principles: Single Responsibility and DRY. Common fluent
 * operations and validation live in one place; concrete builders only define
 * their configuration defaults.</p>
 */
abstract class AbstractComputerBuilder implements ComputerBuilder {
    private String cpu;
    private String gpu;
    private String ram;
    private String storage;
    private String powerSupply;
    private String motherboard;
    private String coolingSystem;
    private boolean hasRGB;
    private int cpuTdp;
    private int gpuTdp;
    private CPU cpuComponent;
    private GPU gpuComponent;
    private Motherboard motherboardComponent;
    private PowerSupply powerSupplyComponent;

    protected AbstractComputerBuilder(ComponentFactory componentFactory, PerformanceTier tier) {
        if (componentFactory == null) {
            throw new IllegalArgumentException("Component factory cannot be null");
        }
        if (tier == null) {
            throw new IllegalArgumentException("Performance tier cannot be null");
        }
        cpu(componentFactory.createCPU(tier));
        gpu(componentFactory.createGPU(tier));
        motherboard(componentFactory.createMotherboard(tier));
        setDefaults(componentFactory, tier);
    }

    private void setDefaults(ComponentFactory componentFactory, PerformanceTier tier) {
        if (tier == PerformanceTier.HIGH) {
            this.ram = "32GB DDR5";
            this.storage = componentFactory.storage(tier);
            this.powerSupply = componentFactory.powerSupply(tier);
            motherboard(componentFactory.createMotherboard(tier));
            this.coolingSystem = componentFactory.coolingSystem(tier);
            this.hasRGB = true;
            return;
        }
        this.ram = "16GB DDR4";
        this.storage = componentFactory.storage(tier);
        this.powerSupply = componentFactory.powerSupply(tier);
        motherboard(componentFactory.createMotherboard(tier));
        this.coolingSystem = componentFactory.coolingSystem(tier);
        this.hasRGB = false;
    }

    @Override public ComputerBuilder cpu(String cpu) { this.cpu = cpu; return this; }
    @Override public ComputerBuilder cpu(CPU cpu) {
        requireComponent(cpu, "CPU");
        this.cpu = cpu.getModel();
        this.cpuTdp = cpu.getTdpWatts();
        this.cpuComponent = cpu;
        return this;
    }
    @Override public ComputerBuilder gpu(String gpu) { this.gpu = gpu; return this; }
    @Override public ComputerBuilder gpu(GPU gpu) {
        requireComponent(gpu, "GPU");
        this.gpu = gpu.getModel();
        this.gpuTdp = gpu.getTdpWatts();
        this.gpuComponent = gpu;
        return this;
    }
    @Override public ComputerBuilder ram(String ram) { this.ram = ram; return this; }
    @Override public ComputerBuilder storage(String storage) { this.storage = storage; return this; }
    @Override public ComputerBuilder powerSupply(String powerSupply) { this.powerSupply = powerSupply; return this; }
    @Override public ComputerBuilder motherboard(String motherboard) { this.motherboard = motherboard; return this; }
    @Override public ComputerBuilder powerSupply(PowerSupply powerSupply) {
        requireComponent(powerSupply, "Power supply");
        this.powerSupplyComponent = powerSupply;
        this.powerSupply = powerSupply.model();
        return this;
    }
    @Override public ComputerBuilder motherboard(Motherboard motherboard) {
        requireComponent(motherboard, "Motherboard");
        this.motherboardComponent = motherboard;
        this.motherboard = motherboard.getModel();
        return this;
    }
    @Override public ComputerBuilder coolingSystem(String coolingSystem) { this.coolingSystem = coolingSystem; return this; }
    @Override public ComputerBuilder withRGB(boolean hasRGB) { this.hasRGB = hasRGB; return this; }

    @Override
    public final Computer build() {
        validateRequiredFields();
        validateSocketCompatibility();
        validatePowerCompatibility();
        return new Computer(cpu, gpu, ram, storage, powerSupply, motherboard, coolingSystem, hasRGB);
    }

    /** Clean Code principle: Fail-Fast and Validated Construction. */
    private void validateRequiredFields() {
        validate(cpu, "CPU");
        validate(gpu, "GPU");
        validate(ram, "RAM");
        validate(storage, "Storage");
        validate(powerSupply, "Power supply");
        validate(motherboard, "Motherboard");
        validate(coolingSystem, "Cooling system");
    }

    private void validate(String value, String component) {
        if (isBlank(value)) {
            throw new IllegalStateException(component + " is required to build a computer.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private void validatePowerCompatibility() {
        int selectedPower = powerSupplyComponent == null
                ? extractWatts(powerSupply) : powerSupplyComponent.wattage();
        int requiredPower = cpuTdp + gpuTdp + 150;
        if (requiredPower > selectedPower) {
            throw new IncompatibleHardwareException("Power Supply (" + selectedPower + "W) is insufficient for "
                    + cpu + " + " + gpu + ". Minimum required: " + requiredPower + "W.");
        }
    }

    private void validateSocketCompatibility() {
        if (cpuComponent != null && motherboardComponent != null
                && !cpuComponent.getSocket().equals(motherboardComponent.getSocket())) {
            throw new IncompatibleHardwareException("Socket mismatch: " + cpuComponent.getModel()
                    + " requires " + cpuComponent.getSocket() + ", but "
                    + motherboardComponent.getModel() + " provides "
                    + motherboardComponent.getSocket() + ".");
        }
    }

    private int extractWatts(String value) {
        String digits = value.replaceFirst("^\\s*(\\d+).*$", "$1");
        return Integer.parseInt(digits);
    }

    private void requireComponent(Object component, String name) {
        if (component == null) {
            throw new IllegalArgumentException(name + " component cannot be null");
        }
    }
}
