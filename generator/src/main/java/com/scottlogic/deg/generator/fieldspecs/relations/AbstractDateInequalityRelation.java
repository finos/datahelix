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

package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeLimit;

import java.time.OffsetDateTime;

abstract class AbstractDateInequalityRelation implements FieldSpecRelations {
    private final Field main;
    private final Field other;

    public AbstractDateInequalityRelation(Field main, Field other) {
        this.main = main;
        this.other = other;
    }

    protected abstract DateTimeLimit dateTimeLimitExtractingFunction(DateTimeRestrictions restrictions);

    protected abstract DateTimeRestrictions appendValueToRestrictions(OffsetDateTime value);

    @Override
    public FieldSpec reduceToRelatedFieldSpec(FieldSpec otherValue) {
        DateTimeLimit limit = dateTimeLimitExtractingFunction(otherValue.getDateTimeRestrictions());

        if (limit != null) {
            OffsetDateTime value = limit.getValue();

            DateTimeRestrictions restrictions = appendValueToRestrictions(value);
            return FieldSpec.Empty.withDateTimeRestrictions(restrictions);
        } else {
            return FieldSpec.Empty;
        }
    }

    @Override
    public Field main() {
        return main;
    }

    @Override
    public Field other() {
        return other;
    }
}
