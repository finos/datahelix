package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;

class ReductiveRowSpecTests {
    @Test
    void equals_objIsNull_returnsFalse() {
        ReductiveRowSpec rowSpec = new ReductiveRowSpec(null, null, null);

        boolean result = rowSpec.equals(null);

        assertFalse(
            "Expected the equality result of a null input to be false but was true",
            result
        );
    }

    @Test
    void equals_objClassIsNotEqualToReductiveRowSpecClass_returnsFalse() {
        ReductiveRowSpec rowSpec = new ReductiveRowSpec(null, null, null);

        boolean result = rowSpec.equals("Test");

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

        boolean result = rowSpec.equals(
            new ReductiveRowSpec(
                new ProfileFields(
                    Arrays.asList(
                        new Field("First Field")
                    )
                ),
                null,
                null
            )
        );

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

        boolean result = rowSpec.equals(
            new ReductiveRowSpec(
                null,
                new HashMap<Field, FieldSpec>() {{
                    put(
                        new Field("Second Field"),
                        FieldSpec.Empty
                    );
                }},
                null
            )
        );

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

        boolean result = rowSpec.equals(
            new ReductiveRowSpec(
                null,
                null,
                new Field("Second Field")
            )
        );

        assertFalse(
            "Expected ReductiveRowSpec equality with differing last fixed field to return false but was true",
            result
        );
    }

    @Test
    void equals_rowSpecHasFieldsAndOtherRowSpecDoesNotHaveFields_returnsFalse() {
        ReductiveRowSpec rowSpec = new ReductiveRowSpec(
            new ProfileFields(
                Arrays.asList(
                    new Field("Test")
                )
            ),
            null,
            new Field("Test")
        );

        boolean result = rowSpec.equals(
            new ReductiveRowSpec(
                null,
                null,
                new Field("Test")
            )
        );

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

        boolean result = rowSpec.equals(
            new ReductiveRowSpec(
                new ProfileFields(
                    Arrays.asList(
                        new Field("First Field"),
                        new Field("Second Field")
                    )
                ),
                null,
                new Field("Test")
            )
        );

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

        boolean result = rowSpec.equals(
             new ReductiveRowSpec(
                new ProfileFields(
                    Arrays.asList(
                        new Field("First Field"),
                        new Field("Second Field")
                    )
                ),
                 null,
                 new Field("First Field")
             )
        );

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

        boolean result = rowSpec.equals(
            new ReductiveRowSpec(
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
            )
        );

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

        boolean result = rowSpec.equals(
            new ReductiveRowSpec(
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
            )
        );

        assertFalse(
            "Expected that when the row spec has a valid last fixed field but the other object last fixed field is null a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_rowSpecLastFixedFieldNullAndOtherObjectHasLastFixedField_returnsFalse() {
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
            null
        );

        boolean result = rowSpec.equals(
            new ReductiveRowSpec(
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
            )
        );

        assertFalse(
            "Expected that when the last fixed field is null and the other object has a valid last fixed field a false value should be returned but was true",
            result
        );
    }
}
