package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.*;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FieldCollectionTests {
    @Test
    void allFieldsAreFixed_lastFixedFieldNotNullAndFixedFieldsSizePlusOneEqualsFieldsSize_returnsTrue() {
        HashMap<Field, FixedField> fixedFields = new HashMap<>();
        fixedFields.put(
            new Field("Second Field"),
            new FixedField(new Field("Second Field"), null, null, null)
        );
        FixedField lastFixedField = new FixedField(
            new Field("First Field"),
            null,
            null,
            null
        );
        FieldCollection fieldCollection = getFieldCollection(
            Arrays.asList("First Field", "Second Field"),
            fixedFields,
            lastFixedField
        );

        boolean result = fieldCollection.allFieldsAreFixed();

        Assert.assertTrue(result);
    }

    @Test
    void allFieldsAreFixed_lastFixedFieldNullAndFixedFieldsSizeEqualToFieldSize_returnsTrue() {
        HashMap<Field, FixedField> fixedFields = new HashMap<>();
        fixedFields.put(
            new Field("First Field"),
            new FixedField(new Field("First Field"), null, null, null)
        );
        fixedFields.put(
            new Field("Second Field"),
            new FixedField(new Field("Second Field"), null, null, null)
        );
        FieldCollection fieldCollection = getFieldCollection(
            Arrays.asList("First Field", "Second Field"),
            fixedFields,
            null
        );

        boolean result = fieldCollection.allFieldsAreFixed();

        Assert.assertTrue(result);
    }

    @Test
    void allFieldsAreFixed_lastFixedFieldNotNullAndFixedFieldsSizePlusOneDoesNotEqualFieldsSize_returnsFalse() {
        HashMap<Field, FixedField> fixedFields = new HashMap<>();
        fixedFields.put(
            new Field("Second Field"),
            new FixedField(new Field("Second Field"), null, null, null)
        );
        FixedField lastFixedField = new FixedField(
            new Field("First Field"),
            null,
            null,
            null
        );
        FieldCollection fieldCollection = getFieldCollection(
            Arrays.asList("First Field", "Second Field", "Third Field"),
            fixedFields,
            lastFixedField
        );

        boolean result = fieldCollection.allFieldsAreFixed();

        Assert.assertFalse(result);
    }

    @Test
    void allFieldsAreFixed_lastFixedFieldNullAndFixedFieldsSizeDoesNotEqualFieldsSize_returnsFalse() {
        HashMap<Field, FixedField> fixedFields = new HashMap<>();
        fixedFields.put(
            new Field("Second Field"),
            new FixedField(new Field("Second Field"), null, null, null)
        );
        FieldCollection fieldCollection = getFieldCollection(
            Arrays.asList("First Field", "Second Field"),
            fixedFields,
            null
        );

        boolean result = fieldCollection.allFieldsAreFixed();

        Assert.assertFalse(result);
    }

    @Test
    void getValuesFromLastFixedField_lastFixedFieldNull_throwsIllegalStateException() {
        FieldCollection fieldCollection = getFieldCollection(
            Arrays.asList("First Field", "Second Field"),
            null,
            null
        );

        Assertions.assertThrows(IllegalStateException.class, fieldCollection::getValuesFromLastFixedField);
    }

    @Test
    void getValuesFromLastFixedField_lastFixedFieldNotNull_returnsStreamOfAllValuesInLastFixedField() {
        List<Integer> values = Arrays.asList(1, 2, 3, 4, 5);
        FieldCollection fieldCollection = getFieldCollection(
            Arrays.asList("First Field", "Second Field"),
            null,
            new FixedField(
                new Field("First Field"),
                Stream.of(values),
                null,
                mock(ReductiveDataGeneratorMonitor.class))
        );

        Stream<Object> result = fieldCollection.getValuesFromLastFixedField();
        final Stream<List<Object>> fixed = result.map(o -> (List<Object>) o);
        final Stream<Object> expectedStream = fixed.flatMap(Collection::stream);

        Assert.assertEquals(values, expectedStream.collect(Collectors.toList()));
    }

    @Test
    void createRowSpecFromFixedValues_lastFixedFieldNull_throwsIllegalStateException() {
        FieldCollection fieldCollection = getFieldCollection(
            Arrays.asList("First Field", "Second Field"),
            null,
            null
        );

        Assertions.assertThrows(IllegalStateException.class, fieldCollection::getValuesFromLastFixedField);
    }

    @Test
    void createRowSpecFromFixedValues_reducerReturnsEmptyOptionalFieldSpec_returnsEmptyStream() {
        ConstraintReducer reducer = mock(ConstraintReducer.class);
        when(reducer.reduceConstraintsToFieldSpec(Matchers.<Collection<AtomicConstraint>>any()))
            .thenReturn(Optional.empty());

        HashMap<Field, FixedField> fixedFields = new HashMap<>();
        FixedField fixedField = new FixedField(
            new Field("First Field"),
            Collections.singleton(null).stream(),
            FieldSpec.Empty,
            mock(ReductiveDataGeneratorMonitor.class)
        );
        fixedFields.put(
            new Field("First Field"),
            fixedField
        );
        FieldCollection fieldCollection = getFieldCollection(
            reducer,
            Collections.emptyList(),
            fixedFields,
            new FixedField(
                new Field("Test"),
                Stream.of(Collections.emptyList()),
                null,
                mock(ReductiveDataGeneratorMonitor.class)
            )
        );

        fixedField.getStream().collect(Collectors.toList());
        Stream<RowSpec> result = fieldCollection.createRowSpecFromFixedValues(
            new ReductiveConstraintNode(
                new TreeConstraintNode(Collections.emptyList(), Collections.emptyList()),
                null
            )
        );

        Assert.assertEquals(result.collect(Collectors.toList()), Collections.emptyList());
    }

    private FieldCollection getFieldCollection(
        ConstraintReducer reducer,
        List<String> fieldNames,
        Map<Field, FixedField> fixedFields,
        FixedField lastFixedField) {
        return new FieldCollection(
            getProfileFieldsFromFieldNames(fieldNames),
            null,
            null,
            reducer,
            new FieldSpecMerger(),
            null,
            null,
            fixedFields,
            lastFixedField,
            mock(ReductiveDataGeneratorMonitor.class)
        );
    }

    private FieldCollection getFieldCollection(
        List<String> fieldNames,
        Map<Field, FixedField> fixedFields,
        FixedField lastFixedField) {
        return new FieldCollection(
            getProfileFieldsFromFieldNames(fieldNames),
            null,
            null,
            null,
            new FieldSpecMerger(),
            null,
            null,
            fixedFields,
            lastFixedField,
            mock(ReductiveDataGeneratorMonitor.class)
        );
    }

    private ProfileFields getProfileFieldsFromFieldNames(List<String> fieldNames) {
        return new ProfileFields(
            fieldNames
                .stream()
                .map(Field::new)
                .collect(Collectors.toList())
        );
    }
}
