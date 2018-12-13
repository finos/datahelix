package com.scottlogic.deg.generator.utils;

import java.util.Random;

public class JavaUtilRandomNumberGenerator implements RandomNumberGenerator {
    private final Random random;

    public JavaUtilRandomNumberGenerator(){
        random = new Random();
    }

    public JavaUtilRandomNumberGenerator(long seed){
        random = new Random(seed);
    }

    @Override
    public int nextInt() {
        return random.nextInt();
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    @Override
    public int nextInt(int lowerInclusive, int upperExclusive) {
        // implementation copied from Random::internalNextInt
        if (lowerInclusive < upperExclusive) {
            int n = upperExclusive - lowerInclusive;
            if (n > 0) {
                return nextInt(n) + lowerInclusive;
            }
            else {  // range not representable as int
                int r;
                do {
                    r = nextInt();
                } while (r < lowerInclusive || r >= upperExclusive);
                return r;
            }
        }
        else {
            return nextInt();
        }
    }

    @Override
    public double nextDouble(double lower, double upper) {
        return random.nextDouble() * (upper - lower) + lower;
    }
}
