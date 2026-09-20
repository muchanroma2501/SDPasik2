# GamingPC Builder Pattern Demo

This project demonstrates the Builder design pattern in Java 17 for assembling gaming and budget personal computers. It uses a fluent API to build `Computer` objects that represent a PC configuration and validates required parts before construction.

## System Requirements

- JDK 17 or newer
- Maven 3.9+

## Folder Structure

```text
GamingPC-Builder/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── com/
                └── astanait/
                    └── gamingpc/
                        ├── Computer.java
                        ├── ComputerBuilder.java
                        ├── BudgetComputerBuilder.java
                        ├── GamingComputerBuilder.java
                        ├── ComputerDirector.java
                        └── Main.java
```

## Build and Run

### 1) Compile the project

```bash
mvn clean compile
```

### 2) Run the program

```bash
java -cp target/classes com.astanait.gamingpc.Main
```

## What the project demonstrates

- Manual construction using fluent method chaining
- Automated construction using the `ComputerDirector`
- Strict validation in `build()` to prevent invalid computer configurations
- Clean, maintainable domain classes for PC assembly

## Example output

The application prints both manually assembled computers and director-driven predefined configurations while also demonstrating validation errors for incomplete builds.
