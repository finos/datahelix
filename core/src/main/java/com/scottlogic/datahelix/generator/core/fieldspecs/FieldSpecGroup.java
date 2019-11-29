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


package com.scottlogic.datahelix.generator.core.fieldspecs;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.core.fieldspecs.relations.EqualToOffsetRelation;
import com.scottlogic.datahelix.generator.core.fieldspecs.relations.EqualToRelation;
import com.scottlogic.datahelix.generator.core.fieldspecs.relations.FieldSpecRelation;
import com.scottlogic.datahelix.generator.core.restrictions.TypedRestrictions;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictions;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class FieldSpecGroup {
    private final Map<Field, FieldSpec> fieldSpecs;

    private final Collection<FieldSpecRelation> relations;

    public FieldSpecGroup(Map<Field, FieldSpec> fieldSpecs, Collection<FieldSpecRelation> relations) {
        validateFieldSpecs(fieldSpecs, relations);
        this.fieldSpecs = fieldSpecs;
        this.relations = relations;
    }

    private void validateFieldSpecs(Map<Field, FieldSpec> fieldSpecs, Collection<FieldSpecRelation> relations) {
        List<FieldSpecRelation> equalToRelations = relations.stream()
            .filter(rel -> rel instanceof EqualToOffsetRelation || rel instanceof EqualToRelation)
            .collect(Collectors.toList());

        for (FieldSpecRelation relation : equalToRelations) {
            Field left = relation.main();
            Field right = relation.other();
            FieldSpec leftSpec = fieldSpecs.get(left);
            FieldSpec rightSpec = fieldSpecs.get(right);

            if (leftSpec instanceof RestrictionsFieldSpec && rightSpec instanceof RestrictionsFieldSpec) {
                RestrictionsFieldSpec leftCast = (RestrictionsFieldSpec) leftSpec;
                RestrictionsFieldSpec rightCast = (RestrictionsFieldSpec) rightSpec;
                TypedRestrictions<?> leftRestrictions = leftCast.getRestrictions();
                TypedRestrictions<?> rightRestrictions = rightCast.getRestrictions();
                if (leftRestrictions instanceof LinearRestrictions<?> && rightRestrictions instanceof LinearRestrictions<?>) {
                    LinearRestrictions<?> leftRestrictionsCast = (LinearRestrictions) leftRestrictions;
                    LinearRestrictions<?> rightRestrictionsCast = (LinearRestrictions) rightRestrictions;
                    if (!leftRestrictionsCast.getGranularity().equals(rightRestrictionsCast.getGranularity())) {
                        throw new IllegalArgumentException("Cannot have two related fields with different granularity."
                        + " Consider using a format restriction instead.");
                    }
                }
            }
        }
    }

    public Map<Field, FieldSpec> fieldSpecs() {
        return fieldSpecs;
    };

    public Collection<FieldSpecRelation> relations() {
        return relations;
    };

}
