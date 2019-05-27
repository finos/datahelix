package com.scottlogic.deg.generator.generation;

public class RicStringGenerator extends RegexStringGenerator {
    public static final int RIC_LENGTH = 7;

    public RicStringGenerator() {
        super("^[A-Z]{1,4}\\.[A-Z]{1,2}$", true);
    }
}
