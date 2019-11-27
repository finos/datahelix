/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.datahelix.generator.common.util;

import java.util.Random;

public class OrderedRandom extends Random {

    private int internalInt = 0;
    private long internalLong = 0;
    private boolean internalBoolean = false;
    private double internalDouble = 0;

    @Override
    public int nextInt(int bound) {
        int returnValue = internalInt % bound;
        internalInt = internalInt + 1;
        return returnValue;
    }

    @Override
    public long nextLong() {
        long returnValue = internalLong;
        internalLong = internalLong + 1;
        return returnValue;
    }

    @Override
    public boolean nextBoolean() {
        boolean returnValue = internalBoolean;
        internalBoolean = !internalBoolean;
        return returnValue;
    }

    @Override
    public double nextDouble() {
        double returnValue = internalDouble;
        internalDouble = Math.nextUp(internalDouble);
        return returnValue;
    }
}
