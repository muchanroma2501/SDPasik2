package com.aitu.sdp.assignment3.bridge;

import com.aitu.sdp.assignment2.Computer;

public interface ReportExporter {
    String exportHeader(String title);

    String exportBody(Computer computer);

    default String exportBody(Computer computer, ReportDetail detail) {
        return exportBody(computer);
    }

    String exportFooter();
}
