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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Returns a FieldSpec that permits only data permitted by all of its inputs
 */
public class FieldSpecMerger {
    private final static List<Class<? extends FieldSpec>> fieldSpecs = new ArrayList<>(Arrays.asList(
        NullOnlyFieldSpec.class,
        WeightedLegalValuesFieldSpec.class,
        LegalValuesFieldSpec.class,
        GeneratorFieldSpec.class,
        RestrictionsFieldSpec.class
    ));

    /**
     * Null parameters are permitted, and are synonymous with an empty FieldSpec
     * <p>
     * Returning an empty Optional conveys that the fields were unmergeable.
     */
    public Optional<FieldSpec> merge(FieldSpec left, FieldSpec right, boolean useFinestGranularityAvailable) {
        int leftOrder = fieldSpecs.indexOf(left.getClass());
        int rightOrder = fieldSpecs.indexOf(right.getClass());

        return leftOrder <= rightOrder
            ? left.merge(right, useFinestGranularityAvailable)
            : right.merge(left, useFinestGranularityAvailable);
    }

}
