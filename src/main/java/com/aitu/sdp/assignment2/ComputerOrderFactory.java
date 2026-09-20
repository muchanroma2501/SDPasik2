package com.aitu.sdp.assignment2;

/**
 * Factory Method creator for complete computer orders.
 *
 * <p>Clean Code principle: Polymorphism over Conditionals. The creator
 * delegates builder selection to subclasses and contains no type conditionals.</p>
 */
public abstract class ComputerOrderFactory {
    protected abstract ComputerBuilder createBuilder();

    public Computer orderComputer() {
        return orderComputer(null, null, null);
    }

    public Computer orderComputer(String ram, String storage, Boolean hasRGB) {
        System.out.println("Ordering computer with " + getClass().getSimpleName() + "...");
        ComputerBuilder builder = createBuilder();
        if (ram != null) {
            builder.ram(ram);
        }
        if (storage != null) {
            builder.storage(storage);
        }
        if (hasRGB != null) {
            builder.withRGB(hasRGB);
        }
        Computer computer = builder.build();
        System.out.println("Computer order completed.");
        return computer;
    }
}
