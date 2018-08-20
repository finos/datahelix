package com.scottlogic.deg.generator.utils;

import java.util.Iterator;

public interface IStringGenerator {

    IStringGenerator intersect(IStringGenerator automaton);
    IStringGenerator complement();

    boolean IsFinite();
    boolean canProduceValues();
    long getValueCount();

    Iterator<String> generateAllValues();

    String generateRandomValue();
    String generateRandomValue(int maxChars);
}
