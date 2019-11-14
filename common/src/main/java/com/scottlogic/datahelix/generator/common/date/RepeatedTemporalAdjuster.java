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

package com.scottlogic.datahelix.generator.common.date;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

public class RepeatedTemporalAdjuster implements TemporalAdjuster {

    private final TemporalAdjuster underlyingAdjuster;

    private final int timesToRepeat;

    public RepeatedTemporalAdjuster(TemporalAdjuster underlyingAdjuster, int timesToRepeat) {
        this.underlyingAdjuster = underlyingAdjuster;
        this.timesToRepeat = timesToRepeat;
    }

    @Override
    public Temporal adjustInto(Temporal temporal) {
        for (int i = 0; i < timesToRepeat; i++) {
            temporal = underlyingAdjuster.adjustInto(temporal);
        }
        return temporal;
    }
}
