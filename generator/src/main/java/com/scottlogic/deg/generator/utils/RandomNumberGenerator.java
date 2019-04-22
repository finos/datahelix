package com.scottlogic.deg.generator.utils;

import java.math.BigDecimal;

public interface RandomNumberGenerator {
    int nextInt();
    int nextInt(int bound);
    int nextInt(int lowerInclusive, int upperExclusive);
    double nextDouble(double lowerInclusive, double upperExclusive);
    BigDecimal nextBigDecimal(BigDecimal lowerInclusive, BigDecimal upperExclusive, int scale);
}
