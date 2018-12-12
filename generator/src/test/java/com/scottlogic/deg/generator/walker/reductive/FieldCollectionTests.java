package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
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
            new FixedField(new Field("Second Field"), null, null)
        );
        FixedField lastFixedField = new FixedField(
            new Field("First Field"),
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
            new FixedField(new Field("First Field"), null, null)
        );
        fixedFields.put(
            new Field("Second Field"),
            new FixedField(new Field("Second Field"), null, null)
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
            new FixedField(new Field("Second Field"), null, null)
        );
        FixedField lastFixedField = new FixedField(
            new Field("First Field"),
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
            new FixedField(new Field("Second Field"), null, null)
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
            new FixedField(new Field("First Field"), Stream.of(values), null)
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

        FixedField fixedField = new FixedField(
            new Field("Second Field"),
            Stream.of(new String[] { "Test" }),
            null
        );
        HashMap<Field, FixedField> fixedFields = new HashMap<>();
        FieldCollection fieldCollection = getFieldCollection(
            reducer,
            null,
            fixedFields,
            new FixedField(new Field("Test"), Stream.of(Collections.emptyList()), null)
        );

        Stream<RowSpec> result = fieldCollection.createRowSpecFromFixedValues(
            new ReductiveConstraintNode(
                new TreeConstraintNode(Collections.emptyList(), Collections.emptyList()),
                null
            )
        );

        Assert.assertEquals(result.collect(Collectors.toList()), Collections.emptyList());
    }

    @Test
    void createRowSpecFromFixedValues_fieldSpecsPerFieldContainsNoEmptyFieldSpecs_returnsRowSpecOfAllValuesForLastFixedField() {
        FieldSpec mustBeNullFieldSpec = new FieldSpec(
            null,
            null,
            null,
            new NullRestrictions(NullRestrictions.Nullness.MUST_BE_NULL),
            new NoTypeRestriction(),
            null,
            null,
            null,
            null
        );
        ConstraintReducer reducer = mock(ConstraintReducer.class);
        when(reducer.reduceConstraintsToFieldSpec(Matchers.<Collection<AtomicConstraint>>any()))
            .thenReturn(Optional.of(mustBeNullFieldSpec));

        final List<String> fieldNames = Arrays.asList("First Field", "Second Field");
        HashMap<Field, FixedField> fixedFields = new HashMap<>();
        FixedField fixedField = new FixedField(
            new Field("First Field"),
            Collections.singleton(null).stream(),
            null
        );
        fixedFields.put(
            new Field("First Field"),
            fixedField
        );
        FieldCollection fieldCollection = getFieldCollection(
            reducer,
            fieldNames,
            fixedFields,
            new FixedField(
                new Field("Second Field"),
                Collections.singleton(null).stream(),
                mustBeNullFieldSpec
            )
        );

        fixedField.getStream().collect(Collectors.toList());
        final Stream<RowSpec> result = fieldCollection.createRowSpecFromFixedValues(
            new ReductiveConstraintNode(
                new TreeConstraintNode(
                    Arrays.asList(
                        new AtomicConstraint[] {
                            new IsNullConstraint(new Field("First Field")),
                            new IsNullConstraint(new Field("Second Field"))
                        }
                    ), Collections.emptyList()),
                null
            )
        );

        Map<Field, FieldSpec> expectedMap = new HashMap<Field, FieldSpec>()
        {{
            put(
                new Field("First Field"),
                mustBeNullFieldSpec
            );
            put(
                new Field("Second Field"),
                mustBeNullFieldSpec
            );
        }};

        final ArrayList<ReductiveRowSpec> second_field = new ArrayList<>(Arrays.asList(
            new ReductiveRowSpec(
                getProfileFieldsFromFieldNames(fieldNames),
                expectedMap,
                new Field("Second Field")
            )
        ));
        Assert.assertEquals(
            result.collect(Collectors.toList()),
            new ArrayList<>(Arrays.asList(
                new ReductiveRowSpec(
                    getProfileFieldsFromFieldNames(fieldNames),
                    expectedMap,
                    new Field("Second Field")
                )
            ))
        );
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
            lastFixedField
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
            lastFixedField
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
