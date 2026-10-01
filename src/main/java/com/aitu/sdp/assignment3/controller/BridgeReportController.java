package com.aitu.sdp.assignment3.controller;

import com.aitu.sdp.assignment2.Computer;
import com.aitu.sdp.assignment2.GamingPCOrderFactory;
import com.aitu.sdp.assignment3.bridge.ComputerReport;
import com.aitu.sdp.assignment3.bridge.FullDiagnosticReport;
import com.aitu.sdp.assignment3.bridge.HtmlReportExporter;
import com.aitu.sdp.assignment3.bridge.JsonReportExporter;
import com.aitu.sdp.assignment3.bridge.ReportExporter;
import com.aitu.sdp.assignment3.bridge.SummarySpecsReport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/reports")
public class BridgeReportController {
    @GetMapping("/export")
    public ResponseEntity<String> export(
            @RequestParam(name = "reportType", defaultValue = "summary") String reportType,
            @RequestParam(name = "format", defaultValue = "json") String format) {
        ReportExporter exporter = createExporter(format);
        ComputerReport report = createReport(reportType, exporter);
        Computer computer = new GamingPCOrderFactory().orderComputer();
        MediaType contentType = "html".equals(format)
                ? MediaType.TEXT_HTML
                : MediaType.APPLICATION_JSON;
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(report.generate(computer));
    }

    private ReportExporter createExporter(String format) {
        return switch (format) {
            case "json" -> new JsonReportExporter();
            case "html" -> new HtmlReportExporter();
            default -> throw invalid("format must be 'json' or 'html'");
        };
    }

    private ComputerReport createReport(String reportType, ReportExporter exporter) {
        return switch (reportType) {
            case "summary" -> new SummarySpecsReport(exporter);
            case "full" -> new FullDiagnosticReport(exporter);
            default -> throw invalid("reportType must be 'summary' or 'full'");
        };
    }

    private ResponseStatusException invalid(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
