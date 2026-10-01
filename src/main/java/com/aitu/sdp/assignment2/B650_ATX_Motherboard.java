package com.aitu.sdp.assignment2;

public final class B650_ATX_Motherboard implements Motherboard {
    @Override
    public String getModel() {
        return "B650 ATX";
    }

    @Override
    public String getSocket() {
        return ComponentCatalog.socketForMotherboard(getModel());
    }
}
