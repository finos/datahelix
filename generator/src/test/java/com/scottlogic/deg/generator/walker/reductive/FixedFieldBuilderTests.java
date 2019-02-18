package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.reducer.ConstraintMapper;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;
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

class FixedFieldBuilderTests {
    @Test
    public void shouldReturnNullWhenAllConstraintsForFieldToFixContradict(){
        ConstraintMapper reducer = mock(ConstraintMapper.class);
        FixFieldStrategy fixFieldStrategy = mock(FixFieldStrategy.class);
        FixedFieldBuilder builder = new FixedFieldBuilder(
            reducer,
            fixFieldStrategy,
            new NoopDataGeneratorMonitor(),
            mock(FieldSpecValueGenerator.class));
        Field field1 = new Field("field1");
        ProfileFields fields = new ProfileFields(Collections.singletonList(field1));
        ReductiveState state = new ReductiveState(fields);
        ReductiveConstraintNode rootNode = new ReductiveConstraintNode(
            new TreeConstraintNode(
                new IsNullConstraint(field1, Collections.emptySet()),
                new IsNullConstraint(field1, Collections.emptySet()).negate()),
            Collections.emptySet());
        when(fixFieldStrategy.getNextFieldToFix(state, rootNode)).thenReturn(field1);
        when(reducer.mapToFieldSpec(any(), any())).thenReturn(Optional.empty());

        FixedField field = builder.findNextFixedField(state, rootNode);

        verify(reducer).mapToFieldSpec(any(), any());
        Assert.assertThat(field, is(nullValue()));
    }

    @Test
    public void shouldReturnFixedFieldWhenRootNodeContainsNoContradictions(){
        ConstraintMapper reducer = mock(ConstraintMapper.class);
        FixFieldStrategy fixFieldStrategy = mock(FixFieldStrategy.class);
        FieldSpecValueGenerator valueGenerator = mock(FieldSpecValueGenerator.class);
        FixedFieldBuilder builder = new FixedFieldBuilder(
            reducer,
            fixFieldStrategy,
            new NoopDataGeneratorMonitor(),
            valueGenerator);
        Field field1 = new Field("field1");
        ProfileFields fields = new ProfileFields(Collections.singletonList(field1));
        ReductiveState state = new ReductiveState(fields);
        ReductiveConstraintNode rootNode = new ReductiveConstraintNode(
            new TreeConstraintNode(new IsNullConstraint(field1, Collections.emptySet())),
            Collections.emptySet());
        when(fixFieldStrategy.getNextFieldToFix(state, rootNode)).thenReturn(field1);
        when(reducer.mapToFieldSpec(any(), any())).thenReturn(Optional.of(FieldSpec.Empty));
        when(valueGenerator.generate(field1, FieldSpec.Empty)).thenReturn(Stream.of(DataBag.empty));

        FixedField field = builder.findNextFixedField(state, rootNode);

        verify(reducer).mapToFieldSpec(any(), any());
        verify(valueGenerator).generate(field1, FieldSpec.Empty);
        Assert.assertThat(field, not(nullValue()));
    }
}