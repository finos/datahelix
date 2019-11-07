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
package com.scottlogic.deg.common.util.defaults;

import com.scottlogic.deg.common.profile.Granularity;

import java.math.BigDecimal;

import static com.scottlogic.deg.common.util.Defaults.*;

public class NumericDefaults implements LinearDefaults<BigDecimal> {

    private static NumericDefaults singleton;
    private NumericDefaults(){ }
    public static synchronized NumericDefaults get() {
        if (singleton == null)
            singleton = new NumericDefaults();
        return singleton;
    }

    @Override
    public BigDecimal min() {
        return NUMERIC_MIN;
    }

    @Override
    public BigDecimal max() {
        return NUMERIC_MAX;
    }

    @Override
    public Granularity<BigDecimal> granularity() {
        return DEFAULT_NUMERIC_GRANULARITY;
    }
}
