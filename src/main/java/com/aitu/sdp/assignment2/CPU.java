package com.aitu.sdp.assignment2;

/** Abstract product for processor components. */
public interface CPU {
    String getModel();

    int getTdpWatts();

    String getSocket();
}
