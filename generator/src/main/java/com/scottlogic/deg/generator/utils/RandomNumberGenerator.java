package com.scottlogic.deg.generator.utils;

public interface RandomNumberGenerator {
    int nextInt();
    int nextInt(int bound);
    int nextInt(int lowerInclusive, int upperExclusive);
    double nextDouble(double lower, double upper);
}
