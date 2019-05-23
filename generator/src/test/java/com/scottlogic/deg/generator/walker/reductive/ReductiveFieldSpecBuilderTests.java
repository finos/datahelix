package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;
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
        ReductiveFieldSpecBuilder builder = new ReductiveFieldSpecBuilder(reducer);
        Field field1 = new Field("field");
        TreeConstraintNode rootNode =
            new TreeConstraintNode(
                new IsNullConstraint(field1, Collections.emptySet()),
                new IsNullConstraint(field1, Collections.emptySet()).negate());
        when(reducer.reduceConstraintsToFieldSpecWithMustContains(any(), any())).thenReturn(Optional.empty());

        Optional<FieldSpec> field = builder.getFieldSpecWithMustContains(rootNode, field1);

        verify(reducer).reduceConstraintsToFieldSpecWithMustContains(any(), any());
        Assert.assertThat(field, is(Optional.empty()));
    }

    @Test
    public void shouldReturnFixedFieldWhenRootNodeContainsNoContradictions(){
        ConstraintReducer reducer = mock(ConstraintReducer.class);
        FieldSpecValueGenerator valueGenerator = mock(FieldSpecValueGenerator.class);
        ReductiveFieldSpecBuilder builder = new ReductiveFieldSpecBuilder(reducer);
        Field field1 = new Field("field");
        TreeConstraintNode rootNode = new TreeConstraintNode(new IsNullConstraint(field1, Collections.emptySet()));
        when(reducer.reduceConstraintsToFieldSpecWithMustContains(any(), any())).thenReturn(Optional.of(FieldSpec.Empty));
        when(valueGenerator.generate(FieldSpec.Empty)).thenReturn(Stream.of(mock(DataBagValue.class)));

        Optional<FieldSpec> field = builder.getFieldSpecWithMustContains(rootNode, field1);

        verify(reducer).reduceConstraintsToFieldSpecWithMustContains(any(), any());
        Assert.assertTrue(field.isPresent());
    }
}