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

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.generator.fieldspecs.whitelist.NullDistributedSet;
import com.scottlogic.deg.generator.generation.fieldvaluesources.CannedValuesFieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.RealNumberFieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.datetime.DateTimeFieldValueSource;
import com.scottlogic.deg.generator.generation.string.generators.RegexStringGenerator;
import com.scottlogic.deg.generator.generation.string.generators.StringGenerator;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.*;

public class StandardFieldValueSourceEvaluator implements FieldValueSourceEvaluator {
    private static final CannedValuesFieldValueSource NULL_ONLY_SOURCE = setupNullOnlySource();

    private static CannedValuesFieldValueSource setupNullOnlySource() {
        return new CannedValuesFieldValueSource(new NullDistributedSet<>());
    }

    public List<FieldValueSource> getFieldValueSources(FieldSpec fieldSpec){

        if (fieldSpec.getWhitelist() != null) {
            List<FieldValueSource> setRestrictionSources =
                getSetRestrictionSources(fieldSpec.getWhitelist());
            if (fieldSpec.isNullable()) {
                return addNullSource(setRestrictionSources);
            }
            return setRestrictionSources;
        }

        List<FieldValueSource> validSources = new ArrayList<>();

        Types typeRestrictions = fieldSpec.getType();

        if (typeRestrictions == NUMERIC) {
            validSources.add(getNumericSource(fieldSpec));
        }

        if (typeRestrictions == STRING) {
            validSources.add(getStringSource(fieldSpec));
        }

        if (typeRestrictions == DATETIME) {
            validSources.add(getDateTimeSource(fieldSpec));
        }

        if (fieldSpec.isNullable()) {
            validSources.add(NULL_ONLY_SOURCE);
        }

        return validSources;
    }

    private List<FieldValueSource> addNullSource(List<FieldValueSource> setRestrictionSources) {
        return Stream.concat(setRestrictionSources.stream(), Stream.of(NULL_ONLY_SOURCE)).collect(Collectors.toList());
    }

    private List<FieldValueSource> getSetRestrictionSources(DistributedSet<Object> whitelist) {
        if (whitelist.distributedSet().isEmpty()){
            return Collections.emptyList();
        }

        return Collections.singletonList(
            new CannedValuesFieldValueSource(whitelist));
    }

    private FieldValueSource getNumericSource(FieldSpec fieldSpec) {
        NumericRestrictions restrictions = fieldSpec.getNumericRestrictions() == null
            ? new NumericRestrictions()
            : fieldSpec.getNumericRestrictions();

        return new RealNumberFieldValueSource(
            restrictions,
            fieldSpec.getBlacklist());
    }

    private FieldValueSource getStringSource(FieldSpec fieldSpec) {
        StringRestrictions stringRestrictions = fieldSpec.getStringRestrictions();

        if (stringRestrictions == null) {
            stringRestrictions = new StringRestrictionsFactory().forMaxLength(1000);
        }

        StringGenerator generator = stringRestrictions.createGenerator();
        if (!fieldSpec.getBlacklist().isEmpty()) {
            RegexStringGenerator blacklistGenerator = RegexStringGenerator.createFromBlacklist(fieldSpec.getBlacklist());

            generator = generator.intersect(blacklistGenerator);
        }

        return generator.asFieldValueSource();
    }

    private FieldValueSource getDateTimeSource(FieldSpec fieldSpec) {
        DateTimeRestrictions restrictions = fieldSpec.getDateTimeRestrictions();

        return new DateTimeFieldValueSource(
            restrictions != null ? restrictions : new DateTimeRestrictions(),
            fieldSpec.getBlacklist());
    }
}
