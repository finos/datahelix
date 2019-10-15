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

package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.fieldvaluesources.*;
import com.scottlogic.deg.generator.generation.fieldvaluesources.datetime.DateTimeFieldValueSource;
import com.scottlogic.deg.generator.generation.string.generators.RegexStringGenerator;
import com.scottlogic.deg.generator.generation.string.generators.StringGenerator;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createDateTimeRestrictions;
import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createNumericRestrictions;

public class FieldValueSourceEvaluator {
    private static final FieldValueSource NULL_ONLY_SOURCE = new NullOnlySource();

    public FieldValueSource getFieldValueSources(FieldType type, FieldSpec fieldSpec){

        Optional<FieldValueSource> source = getSource(type, fieldSpec);

        if (!fieldSpec.isNullable()){
            return source
                .orElseThrow(() -> new UnsupportedOperationException("Cannot get fieldValueSource for contradictory fieldspec"));
        }

        if (!source.isPresent()){
            return NULL_ONLY_SOURCE;
        }

        return new NullAppendingValueSource(source.get());
    }

    private Optional<FieldValueSource> getSource(FieldType type, FieldSpec fieldSpec) {
        if (fieldSpec.getWhitelist() != null){
            if (fieldSpec.getWhitelist().isEmpty()){
                return Optional.empty();
            }

            return Optional.of(new CannedValuesFieldValueSource(fieldSpec.getWhitelist()));
        }

        return Optional.of(getRestrictionSource(type, fieldSpec));
    }

    private FieldValueSource getRestrictionSource(FieldType type, FieldSpec fieldSpec) {
        switch (type) {
            case DATETIME:
                return getDateTimeSource(fieldSpec);
            case STRING:
                return getStringSource(fieldSpec);
            case NUMERIC:
                return getNumericSource(fieldSpec);
                default:
                    throw new UnsupportedOperationException("unexpected type");
        }
    }

    private FieldValueSource getNumericSource(FieldSpec fieldSpec) {
        LinearRestrictions<BigDecimal> restrictions = (LinearRestrictions<BigDecimal>) fieldSpec.getRestrictions();

        return new RealNumberFieldValueSource(restrictions, fieldSpec.getBlacklist());
    }

    private FieldValueSource getStringSource(FieldSpec fieldSpec) {
        StringRestrictions stringRestrictions = (StringRestrictions) fieldSpec.getRestrictions();

        StringGenerator generator = stringRestrictions.createGenerator();
        if (!fieldSpec.getBlacklist().isEmpty()) {
            RegexStringGenerator blacklistGenerator = RegexStringGenerator.createFromBlacklist(fieldSpec.getBlacklist());
            return generator.intersect(blacklistGenerator);
        }

        return generator;
    }

    private FieldValueSource getDateTimeSource(FieldSpec fieldSpec) {
        LinearRestrictions<OffsetDateTime> restrictions = (LinearRestrictions<OffsetDateTime>) fieldSpec.getRestrictions();
        Set<OffsetDateTime> blacklist = fieldSpec.getBlacklist().stream().map(d -> (OffsetDateTime) d).collect(Collectors.toSet());
        return new DateTimeFieldValueSource(restrictions, blacklist);
    }
}
