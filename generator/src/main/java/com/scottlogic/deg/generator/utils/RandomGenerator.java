package com.scottlogic.deg.generator.utils;

import java.util.Random;

public class RandomGenerator implements IRandomGenerator {

    private final Random random;

    public RandomGenerator(){
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public int nextInt() {
        return random.nextInt();
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
}

