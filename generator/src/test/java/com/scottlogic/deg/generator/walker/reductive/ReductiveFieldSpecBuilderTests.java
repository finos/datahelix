package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
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
        TreeConstraintNode rootNode =
            new TreeConstraintNode(
                new IsNullConstraint(field1, Collections.emptySet()),
                new IsNullConstraint(field1, Collections.emptySet()).negate());

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
        TreeConstraintNode rootNode = new TreeConstraintNode(new IsNullConstraint(field1, Collections.emptySet()));
        when(valueGenerator.generate(FieldSpec.Empty)).thenReturn(Stream.of(mock(DataBagValue.class)));
        when(valueGenerator.generate(field1, FieldSpec.Empty)).thenReturn(Stream.of(DataBag.empty));

        Set<FieldSpec> field = builder.getDecisionFieldSpecs(rootNode, field1);

        Assert.assertEquals(1, field.size());
    }
}