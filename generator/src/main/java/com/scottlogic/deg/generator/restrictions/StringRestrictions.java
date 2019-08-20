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

package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.generation.string.streamy.StreamStringGenerator;

public interface StringRestrictions extends TypedRestrictions {
    MergeResult<StringRestrictions> intersect(StringRestrictions other);

    @Override
    default boolean isInstanceOf(Object o) {
        return IsOfTypeConstraint.Types.STRING.isInstanceOf(o);
    }

    boolean match(String x);

    @Override
    default boolean match(Object x) {
        return isInstanceOf(x) && match((String) x);
    }

    StreamStringGenerator createGenerator();
}


