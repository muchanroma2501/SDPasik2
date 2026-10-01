package com.aitu.sdp.assignment3.bridge;

import com.aitu.sdp.assignment2.Computer;

public class SummarySpecsReport extends ComputerReport {
    public SummarySpecsReport(ReportExporter exporter) {
        super(exporter);
    }

    @Override
    public String generate(Computer computer) {
        return generateReport("Summary PC Specifications", computer, ReportDetail.SUMMARY);
    }
}
