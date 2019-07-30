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

package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.ConstraintNodeBuilder;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class ReductiveFieldSpecBuilderTests {
    @Test
    public void shouldReturnNullWhenAllConstraintsForFieldToFixContradict(){
        ConstraintReducer reducer = mock(ConstraintReducer.class);
        when(reducer.reduceConstraintsToFieldSpec(any())).thenReturn(Optional.empty());
        ReductiveFieldSpecBuilder builder = new ReductiveFieldSpecBuilder(reducer, mock(FieldSpecMerger.class));
        Field field1 = new Field("field");
        ConstraintNode rootNode =
            new ConstraintNodeBuilder().addAtomicConstraints(new IsNullConstraint(field1), new IsNullConstraint(field1).negate()).createConstraintNode();

        Set<FieldSpec> field = builder.getDecisionFieldSpecs(rootNode, field1);

        Assert.assertEquals(0, field.size());
    }

    @Test
    public void shouldReturnFixedFieldWhenRootNodeContainsNoContradictions(){
        ConstraintReducer reducer = mock(ConstraintReducer.class);
        when(reducer.reduceConstraintsToFieldSpec(any())).thenReturn(Optional.of(mock(FieldSpec.class)));
        FieldSpecValueGenerator valueGenerator = mock(FieldSpecValueGenerator.class);
        ReductiveFieldSpecBuilder builder = new ReductiveFieldSpecBuilder(reducer, mock(FieldSpecMerger.class));
        Field field1 = new Field("field");
        ConstraintNode rootNode = new ConstraintNodeBuilder().addAtomicConstraints(new IsNullConstraint(field1)).createConstraintNode();
        when(valueGenerator.generate(FieldSpec.Empty)).thenReturn(Stream.of(mock(DataBagValue.class)));

        Set<FieldSpec> field = builder.getDecisionFieldSpecs(rootNode, field1);

        Assert.assertEquals(1, field.size());
    }
}