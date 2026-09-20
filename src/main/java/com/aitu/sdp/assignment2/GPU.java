package com.aitu.sdp.assignment2;

/** Abstract product for graphics components. */
public interface GPU {
    String getModel();

    int getTdpWatts();

    default int getTDP() {
        return getTdpWatts();
    }
}
