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

import com.scottlogic.datahelix.generator.common.profile.DateTimeGranularity;
import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.common.profile.Granularity;
import com.scottlogic.datahelix.generator.common.profile.NumericGranularity;
import com.scottlogic.datahelix.generator.common.util.defaults.DateTimeDefaults;
import com.scottlogic.datahelix.generator.common.util.defaults.NumericDefaults;

public class GranularityUtils {
    public static Granularity readGranularity(FieldType type, String offsetUnit) {
        switch (type) {
            case NUMERIC:
                return offsetUnit != null
                    ? NumericGranularity.create(offsetUnit)
                    : NumericDefaults.get().granularity();
            case DATETIME:
                return offsetUnit != null
                    ? DateTimeGranularity.create(offsetUnit)
                    : DateTimeDefaults.get().granularity();

            default:
                throw new UnsupportedOperationException("Attempt to find granularity for an unsupported type");
        }
    }
}
