package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.databags.DataBag;
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
        ReductiveFieldSpecBuilder builder = new ReductiveFieldSpecBuilder(
            reducer,
            new NoopDataGeneratorMonitor(),
            mock(FieldSpecValueGenerator.class));
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
        ReductiveFieldSpecBuilder builder = new ReductiveFieldSpecBuilder(
            reducer,
            new NoopDataGeneratorMonitor(),
            valueGenerator);
        Field field1 = new Field("field");
        TreeConstraintNode rootNode = new TreeConstraintNode(new IsNullConstraint(field1, Collections.emptySet()));
        when(reducer.reduceConstraintsToFieldSpecWithMustContains(any(), any())).thenReturn(Optional.of(FieldSpec.Empty));
        when(valueGenerator.generate(field1, FieldSpec.Empty)).thenReturn(Stream.of(DataBag.empty));

        Optional<FieldSpec> field = builder.getFieldSpecWithMustContains(rootNode, field1);

        verify(reducer).reduceConstraintsToFieldSpecWithMustContains(any(), any());
        Assert.assertTrue(field.isPresent());
    }
}