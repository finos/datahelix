package com.scottlogic.deg.generator.Utils;

import com.scottlogic.deg.generator.utils.IRandomGenerator;

import java.util.Random;

public class TestRandomGenerator implements IRandomGenerator {

    private final Random random;

    TestRandomGenerator()
    {
        random = new Random(0); // Fixed seed
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
