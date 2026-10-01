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

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/reports")
public class BridgeReportController {
    private static final Map<String, Supplier<ReportExporter>> EXPORTERS = Map.of(
            "json", JsonReportExporter::new,
            "html", HtmlReportExporter::new);
    private static final Map<String, Function<ReportExporter, ComputerReport>> REPORTS = Map.of(
            "summary", SummarySpecsReport::new,
            "full", FullDiagnosticReport::new);

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
        Supplier<ReportExporter> exporterFactory = EXPORTERS.get(format);
        if (exporterFactory == null) {
            throw invalid("format must be 'json' or 'html'");
        }
        return exporterFactory.get();
    }

    private ComputerReport createReport(String reportType, ReportExporter exporter) {
        Function<ReportExporter, ComputerReport> reportFactory = REPORTS.get(reportType);
        if (reportFactory == null) {
            throw invalid("reportType must be 'summary' or 'full'");
        }
        return reportFactory.apply(exporter);
    }

    private ResponseStatusException invalid(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
