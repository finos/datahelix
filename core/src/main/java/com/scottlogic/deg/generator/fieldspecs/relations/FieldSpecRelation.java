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

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.profile.constraints.Constraint;

public interface FieldSpecRelation extends Constraint {

    /**
     * Creates a field spec from the current state of the passed in FieldSpec.
     *
     * The implementation of this interface should define what the reduced FieldSpec looks like.
     *
     * @param otherFieldSpec
     * @return
     */
    FieldSpec createModifierFromOtherFieldSpec(FieldSpec otherFieldSpec);

    FieldSpec createModifierFromOtherValue(DataBagValue otherFieldGeneratedValue);

    FieldSpecRelation inverse();

    Field main();

    Field other();

}
