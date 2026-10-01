# SDPasik2 — PC Configurator and Design Patterns

Spring Boot application for assembling and configuring PC builds. The project demonstrates creational and structural design patterns in the same domain model, and serves the configurator UI from `src/main/resources/static/index.html`.

## Requirements

- JDK 17+
- Maven Wrapper (included) or Maven 3.9+

## Run the application

From the repository root:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Open <http://localhost:8080> for the PC configurator. To build the executable JAR:

```bash
./mvnw clean package
java -jar target/sdpasik2-1.0.0.jar
```

## Design pattern assignments

### Assignments 1 & 2 — Creational patterns

- **Factory Method:** `ComputerOrderFactory` delegates gaming, budget, and workstation builds to their specialized order factories.
- **Abstract Factory:** `ComponentFactory` creates compatible component families. `IntelNvidiaComponentFactory` and `AMDComponentFactory` share data from `ComponentCatalog`.
- **Builder:** `ComputerBuilder` and its implementations assemble and validate typed hardware before creating an immutable `Computer`.

### Assignment 3 — Bridge structural pattern

- **Abstraction:** `ComputerReport`, refined by `SummarySpecsReport` and `FullDiagnosticReport`.
- **Implementor:** `ReportExporter`, implemented by `JsonReportExporter` and `HtmlReportExporter`.
- **Composition and runtime switching:** `ComputerReport` holds a `ReportExporter` reference and can switch it using `setExporter`.
- The startup `BridgePatternDemo` prints a JSON report, switches the same report to HTML, and prints the result.

## REST API

All endpoints use the `/api` base path.

| Method | Endpoint | Purpose |
|---|---|---|
| `GET` | `/api/pc/presets` | Return gaming, budget, and workstation preset configurations. |
| `GET` | `/api/pc/catalog?ecosystem=intel_nvidia` | Return the available components for `intel_nvidia` or `amd`. |
| `POST` | `/api/pc/custom` | Build a custom PC from a JSON component selection. |
| `POST` | `/api/pc/build` | Compatibility endpoint for a named build pipeline. |
| `GET` | `/api/reports/export?reportType=summary&format=html` | Export a `summary` or `full` report as `html` or `json`. |

Example custom-build request:

```json
{
  "ecosystem": "intel_nvidia",
  "cpu": "Intel Core i7-14700K",
  "gpu": "NVIDIA GeForce RTX 4070 Ti 12GB",
  "motherboard": "Z790 ATX",
  "ram": "32GB DDR5",
  "storage": "2TB NVMe Gen4 SSD",
  "powerSupply": "850W 80+ Gold",
  "hasRGB": true
}
```

The catalog endpoint is the source for component model names used in custom requests. The server validates family selection, CPU/motherboard socket compatibility, and PSU headroom.

## Package structure

```text
com.aitu.sdp
├── assignment2
│   ├── component abstractions, implementations, catalog, and builders
│   └── controller
│       ├── PCConfiguratorController
│       └── PerformanceCatalog
└── assignment3
    ├── bridge
    │   ├── ComputerReport and report types
    │   └── ReportExporter and JSON/HTML implementations
    └── controller
        └── BridgeReportController
```

`com.aitu.sdp.assignment2.Main` is the Spring Boot entry point and scans `com.aitu.sdp`, so both assignment controllers are registered.

## Clean Code principles

- **Single Responsibility:** Builders construct configurations, factories select families, controllers handle HTTP, and exporters format reports.
- **Open/Closed:** New exporter strategies implement `ReportExporter`; report abstractions depend on that interface rather than concrete formats.
- **DRY:** `ComponentCatalog` centralizes model, socket, TDP, PSU, storage, and cooling data used by factories and the API catalog.
- **Encapsulation:** `Computer` stores typed hardware objects and exposes display names through getters; `Storage`, `CoolingSystem`, and `PowerSupply` are validated value types.
- **Fail fast:** Invalid components and incomplete or incompatible builds are rejected during resolution or construction rather than producing inconsistent specifications.
