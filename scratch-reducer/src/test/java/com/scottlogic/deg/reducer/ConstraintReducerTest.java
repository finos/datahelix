package com.scottlogic.deg.reducer;

import com.scottlogic.deg.constraint.AmongConstraint;
import com.scottlogic.deg.constraint.IConstraint;
import com.scottlogic.deg.constraint.NumericLimitConstConstraint;
import com.scottlogic.deg.constraint.TypeConstraint;
import com.scottlogic.deg.input.Field;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class ConstraintReducerTest {
    private final ConstraintReducer constraintReducer = new ConstraintReducer();

    @Test
    public void test() {
        var quantity = new Field("quantity");
        var country = new Field("country");
        var city = new Field("city");
        final Iterable<IConstraint> constraints = List.of(
                new NumericLimitConstConstraint<>(quantity, 0, NumericLimitConstConstraint.LimitType.Min, Integer.class),
                new NumericLimitConstConstraint<>(quantity, 10, NumericLimitConstConstraint.LimitType.Max, Integer.class),
                new AmongConstraint<>(country, Set.of("UK", "US"), String.class),
                new TypeConstraint<>(city, String.class)
        );
        constraintReducer.getReducedConstraints(constraints);
    }
}
