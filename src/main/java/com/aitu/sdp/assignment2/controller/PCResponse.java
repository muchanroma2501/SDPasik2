package com.aitu.sdp.assignment2.controller;

import java.util.Map;

/** JSON response containing the immutable computer specifications and pattern trace. */
public record PCResponse(Map<String, String> specs, String validationStatus,
                         String architecturalHighlights, String name,
                         int requiredPowerWatts) {
}
