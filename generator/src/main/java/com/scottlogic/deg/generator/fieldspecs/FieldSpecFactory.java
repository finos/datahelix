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

package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.restrictions.TypedRestrictions;

import java.util.Collections;

import static com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory.forMaxLength;
import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createDefaultDateTimeRestrictions;
import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createDefaultNumericRestrictions;

public class FieldSpecFactory {
    public static final NullOnlyFieldSpec NULL_ONLY_FIELD_SPEC = new NullOnlyFieldSpec();

    public static WhitelistFieldSpec fromList(DistributedList<Object> whitelist) {
        return new WhitelistFieldSpec(whitelist, true);
    }

    public static RestrictionsFieldSpec fromRestriction(TypedRestrictions restrictions) {
        return new RestrictionsFieldSpec(restrictions, true, Collections.emptySet());
    }

    public static RestrictionsFieldSpec fromType(FieldType type) {
        switch (type) {
            case NUMERIC:
                return new RestrictionsFieldSpec(createDefaultNumericRestrictions(), true, Collections.emptySet());
            case DATETIME:
                return new RestrictionsFieldSpec(createDefaultDateTimeRestrictions(), true, Collections.emptySet());
            case STRING:
                return new RestrictionsFieldSpec(forMaxLength(1000), true, Collections.emptySet());
            default:
                throw new IllegalArgumentException("Unable to create FieldSpec from type " + type.name());
        }
    }

    public static NullOnlyFieldSpec nullOnly() {
        return NULL_ONLY_FIELD_SPEC;
    }
}
