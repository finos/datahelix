package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsNullConstraint;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

class FieldSpecFactoryTests {
    @Test
    void toMustContainRestrictionFieldSpec_constraintsContainsNotConstraint_returnsMustContainsRestrictionWithNotConstraint() {
        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();
        Collection<AtomicConstraint> constraints = Collections.singletonList(
            new IsNullConstraint(new Field("Test"), Collections.emptySet()).negate()
        );

        FieldSpec fieldSpec = fieldSpecFactory.toMustContainRestrictionFieldSpec(constraints);

        FieldSpec expectedFieldSpec = FieldSpec.Empty.withMustContainRestriction(
            new MustContainRestriction(
                new HashSet<FieldSpec>() {{
                    add(
                        FieldSpec.Empty.withNullRestrictions(
                            new NullRestrictions(NullRestrictions.Nullness.MUST_NOT_BE_NULL),
                            null
                        )
                    );
                }}
            )
        );

        Assert.assertEquals(fieldSpec, expectedFieldSpec);
    }
}
