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
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;

import java.time.OffsetDateTime;

class BeforeDateRelation extends AbstractDateInequalityRelation {

    public BeforeDateRelation(Field main, Field other) {
        super(main, other);
    }

    @Override
    public DateTimeRestrictions.DateTimeLimit dateTimeLimitExtractingFunction(DateTimeRestrictions restrictions) {
        return restrictions.min;
    }

    @Override
    protected void appendValueToRestrictions(DateTimeRestrictions restrictions, OffsetDateTime value) {
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(value, false);
    }

    @Override
    public FieldSpecRelations inverse() {
        return new AfterDateRelation(other(), main());
    }
}
