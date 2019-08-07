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

public interface FieldSpecRelations {

    /**
     * Produce a field spec similar to how atomic constraints create fieldspecs.
     * This fieldspec should contain a barebones set of restrictions, with only the
     * implementation specific bound included.
     *
     * An example would be for (a:Int > b:Int), where a and b have many restrictions across different types.
     * The resultant fieldspec would take the fieldspec of a, and extract ONLY the RELEVANT Int information:
     *
     * a:Int maximum value, which is the maximum bound of a minus a unit (for ints, 1).
     * This ensures that the resultant fieldspec, when merged with b, will guarantee that a can choose a value
     * that respects the inputted relational constraint.
     *
     * @param otherValue
     * @return
     */
    FieldSpec reduceToRelatedFieldSpec(FieldSpec otherValue);

    FieldSpecRelations inverse();

    Field main();

    Field other();

}
