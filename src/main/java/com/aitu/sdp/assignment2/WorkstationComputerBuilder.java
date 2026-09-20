package com.aitu.sdp.assignment2;

/** Builder configuration for creator-focused workstation systems. */
public final class WorkstationComputerBuilder extends AbstractComputerBuilder {
    public WorkstationComputerBuilder(ComponentFactory componentFactory) {
        super(componentFactory, PerformanceTier.HIGH);
        ram("64GB DDR5")
                .storage("2TB NVMe Gen4 SSD")
                .powerSupply("1000W 80+ Gold")
                .motherboard("Z790 ATX")
                .coolingSystem("360mm AIO Liquid Cooling")
                .withRGB(false);
    }
}
