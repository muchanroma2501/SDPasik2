package com.aitu.sdp.assignment2;

public final class B760_MicroATX_Motherboard implements Motherboard {
    @Override
    public String getModel() {
        return "B760 Micro-ATX";
    }

    @Override
    public String getSocket() {
        return ComponentCatalog.socketForMotherboard(getModel());
    }
}
