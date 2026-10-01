package com.aitu.sdp.assignment3.bridge;

import com.aitu.sdp.assignment2.Computer;

import java.util.Map;

public class HtmlReportExporter implements ReportExporter {
    @Override
    public String exportHeader(String title) {
        return "<!doctype html><html lang=\"en\"><head><meta charset=\"UTF-8\">"
                + "<title>" + escapeHtml(title) + "</title></head><body><main><h1>"
                + escapeHtml(title) + "</h1><table><tbody>";
    }

    @Override
    public String exportBody(Computer computer) {
        return exportBody(computer, ReportDetail.FULL_DIAGNOSTIC);
    }

    @Override
    public String exportBody(Computer computer, ReportDetail detail) {
        Map<String, String> specifications = ReportSpecifications.forComputer(computer, detail);
        StringBuilder body = new StringBuilder();
        for (Map.Entry<String, String> specification : specifications.entrySet()) {
            body.append("<tr><th>")
                    .append(escapeHtml(specification.getKey()))
                    .append("</th><td>")
                    .append(escapeHtml(specification.getValue()))
                    .append("</td></tr>");
        }
        return body.toString();
    }

    @Override
    public String exportFooter() {
        return "</tbody></table></main></body></html>";
    }

    private String escapeHtml(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
