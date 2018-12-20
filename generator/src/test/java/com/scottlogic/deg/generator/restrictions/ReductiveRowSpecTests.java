package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.test_utils.EqualityComparer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.*;

class ReductiveRowSpecTests {
    private EqualityComparer reductiveRowSpecComparer = new ReductiveRowSpecEqualityComparer();

    @Test
    void equals_objIsNull_returnsFalse() {
        ReductiveRowSpec rowSpec = new ReductiveRowSpec(null, null, null);

        boolean result = reductiveRowSpecComparer.equals(rowSpec, null);

        assertFalse(
            "Expected the equality result of a null input to be false but was true",
            result
        );
    }

    @Test
    void equals_objClassIsNotEqualToReductiveRowSpecClass_returnsFalse() {
        ReductiveRowSpec rowSpec = new ReductiveRowSpec(null, null, null);

        boolean result = reductiveRowSpecComparer.equals(rowSpec, "Test");

        assertFalse(
            "Expected the equality result of an input that is not a ReductiveRowSpec to be false but was true",
            result
        );
    }

    @Test
    void equals_profileFieldsAreNotEqual_returnsFalse() {
        ReductiveRowSpec rowSpec = new ReductiveRowSpec(
            new ProfileFields(
                Arrays.asList(
                    new Field("First Field"),
                    new Field("Second Field")
                )
            ),
            null,
            null
        );
        ReductiveRowSpec otherRowSpec = new ReductiveRowSpec(
            new ProfileFields(
                Collections.singletonList(
                    new Field("First Field")
                )
            ),
            null,
            null
        );

        boolean result = reductiveRowSpecComparer.equals(rowSpec, otherRowSpec);

        assertFalse(
            "Expected ReductiveRowSpec equality with differing ProfileFields to return false but was true",
            result
        );
    }

    @Test
    void equals_fieldToFieldSpecsAreNotEqual_returnsFalse() {
        ReductiveRowSpec rowSpec = new ReductiveRowSpec(
            null,
            new HashMap<Field, FieldSpec>() {{
                put(
                    new Field("First Field"),
                    new FieldSpec(
                        null,
                        null,
                        null,
                        new NullRestrictions(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                );
            }},
            null
        );
        ReductiveRowSpec otherRowSpec = new ReductiveRowSpec(
            null,
            new HashMap<Field, FieldSpec>() {{
                put(
                    new Field("Second Field"),
                    FieldSpec.Empty
                );
            }},
            null
        );

        boolean result = reductiveRowSpecComparer.equals(rowSpec, otherRowSpec);

        assertFalse(
            "Expected ReductiveRowSpec equality with differing field specs to return false but was true",
            result
        );
    }

    @Test
    void equals_lastFixedFieldsAreNotEqual_returnsFalse() {
        ReductiveRowSpec rowSpec = new ReductiveRowSpec(
            null,
            null,
            new Field("First Field")
        );
        ReductiveRowSpec otherRowSpec = new ReductiveRowSpec(
            null,
            null,
            new Field("Second Field")
        );

        boolean result = reductiveRowSpecComparer.equals(rowSpec, otherRowSpec);

        assertFalse(
            "Expected ReductiveRowSpec equality with differing last fixed field to return false but was true",
            result
        );
    }

    @Test
    void equals_rowSpecHasFieldsAndOtherRowSpecDoesNotHaveFields_returnsFalse() {
        ReductiveRowSpec rowSpec = new ReductiveRowSpec(
            new ProfileFields(
                Collections.singletonList(
                    new Field("Test")
                )
            ),
            null,
            new Field("Test")
        );
        ReductiveRowSpec otherRowSpec = new ReductiveRowSpec(
            null,
            null,
            new Field("Test")
        );

        boolean result = reductiveRowSpecComparer.equals(rowSpec, otherRowSpec);

        assertFalse(
            "Expected ReductiveRowSpec with fields while the other object fields is null to return false but was true",
            result
        );
    }

    @Test
    void equals_rowSpecFieldsAreNullAndOtherRowSpecHasFields_returnsFalse() {
        ReductiveRowSpec rowSpec = new ReductiveRowSpec(
            null,
            null,
            new Field("Test")
        );
        ReductiveRowSpec otherRowSpec = new ReductiveRowSpec(
            new ProfileFields(
                Arrays.asList(
                    new Field("First Field"),
                    new Field("Second Field")
                )
            ),
            null,
            new Field("Test")
        );

        boolean result = reductiveRowSpecComparer.equals(rowSpec, otherRowSpec);

        assertFalse(
            "Expected ReductiveRowSpec with null fields while the other object has fields should return false but was true",
            result
        );
    }

    @Test
    void equals_rowSpecHasFieldToFieldSpecAndOtherRowSpecDoesNotHaveFieldToFieldSpec_returnsFalse() {
        ReductiveRowSpec rowSpec = new ReductiveRowSpec(
            new ProfileFields(
                Arrays.asList(
                    new Field("First Field"),
                    new Field("Second Field")
                )
            ),
            new HashMap<Field, FieldSpec>() {{
                put(
                    new Field("First Field"),
                    new FieldSpec(
                        null,
                        null,
                        null,
                        new NullRestrictions(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                );
            }},
            new Field("First Field")
        );
        ReductiveRowSpec otherRowSpec = new ReductiveRowSpec(
            new ProfileFields(
                Arrays.asList(
                    new Field("First Field"),
                    new Field("Second Field")
                )
            ),
             null,
             new Field("First Field")
         );

        boolean result = reductiveRowSpecComparer.equals(rowSpec, otherRowSpec);

        assertFalse(
            "Expected ReductiveRowSpec with a field to field spec while the other object field to field spec is null should return false but was true",
            result
        );
    }

    @Test
    void equals_rowSpecFieldToFieldSpecNullAndOtherRowSpecHasFieldToFieldSpec_returnsFalse() {
        ReductiveRowSpec rowSpec = new ReductiveRowSpec(
            new ProfileFields(
                Arrays.asList(
                    new Field("First Field"),
                    new Field("Second Field")
                )
            ),
            null,
            new Field("First Field")
        );
        ReductiveRowSpec otherRowSpec = new ReductiveRowSpec(
            new ProfileFields(
                Arrays.asList(
                    new Field("First Field"),
                    new Field("Second Field")
                )
            ),
            new HashMap<Field, FieldSpec>() {{
                put(
                    new Field("First Field"),
                    new FieldSpec(
                        null,
                        null,
                        null,
                        new NullRestrictions(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                );
            }},
            new Field("First Field")
        );

        boolean result = reductiveRowSpecComparer.equals(rowSpec, otherRowSpec);

        assertFalse(
            "Expected ReductiveRowSpec with null field to field spec while other object has a valid field to field spec should return false but was true",
            result
        );
    }

    @Test
    void equals_rowSpecHasLastFixedFieldAndOtherObjectLastFixedFieldNull_returnsFalse() {
        ReductiveRowSpec rowSpec = new ReductiveRowSpec(
            new ProfileFields(
                Arrays.asList(
                    new Field("First Field"),
                    new Field("Second Field")
                )
            ),
            new HashMap<Field, FieldSpec>() {{
                put(
                    new Field("First Field"),
                    new FieldSpec(
                        null,
                        null,
                        null,
                        new NullRestrictions(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                );
            }},
            new Field("First Field")
        );
        ReductiveRowSpec otherRowSpec = new ReductiveRowSpec(
            new ProfileFields(
                Arrays.asList(
                    new Field("First Field"),
                    new Field("Second Field")
                )
            ),
            new HashMap<Field, FieldSpec>() {{
                put(
                    new Field("First Field"),
                    new FieldSpec(
                        null,
                        null,
                        null,
                        new NullRestrictions(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                );
            }},
            null
        );

        boolean result = reductiveRowSpecComparer.equals(rowSpec, otherRowSpec);

        assertFalse(
            "Expected that when the row spec has a valid last fixed field but the other object last fixed field is null a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_rowSpecPropertiesAreAllEqual_returnsTrue() {
        ReductiveRowSpec rowSpec = new ReductiveRowSpec(
            new ProfileFields(
                Arrays.asList(
                    new Field("First Field"),
                    new Field("Second Field")
                )
            ),
            new HashMap<Field, FieldSpec>() {{
                put(
                    new Field("First Field"),
                    new FieldSpec(
                        null,
                        null,
                        null,
                        new NullRestrictions(NullRestrictions.Nullness.MUST_BE_NULL),
                        new NoTypeRestriction(),
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                );
            }},
            new Field("Second Field")
        );
        ReductiveRowSpec otherRowSpec = new ReductiveRowSpec(
            new ProfileFields(
                Arrays.asList(
                    new Field("First Field"),
                    new Field("Second Field")
                )
            ),
            new HashMap<Field, FieldSpec>() {{
                put(
                    new Field("First Field"),
                    new FieldSpec(
                        null,
                        null,
                        null,
                        new NullRestrictions(NullRestrictions.Nullness.MUST_BE_NULL),
                        new NoTypeRestriction(),
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                );
            }},
            new Field("Second Field")
        );

        boolean result = reductiveRowSpecComparer.equals(rowSpec, otherRowSpec);

        assertTrue(
            "Expected row specs to be equal but were not",
            result
        );
    }
}
