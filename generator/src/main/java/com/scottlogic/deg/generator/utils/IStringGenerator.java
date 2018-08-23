package com.scottlogic.deg.generator.utils;

import java.util.Iterator;

public interface IStringGenerator {

    IStringGenerator intersect(IStringGenerator stringGenerator);
    IStringGenerator complement();

    boolean IsFinite();
    boolean canProduceValues();
    long getValueCount();
    String getMatchedString(int indexOrder);

    Iterator<String> generateAllValues();

    String generateRandomValue();
    String generateRandomValue(int maxChars);
}
