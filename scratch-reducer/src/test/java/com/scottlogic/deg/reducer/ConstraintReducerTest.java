package com.scottlogic.deg.reducer;

import com.scottlogic.deg.constraint.AmongConstraint;
import com.scottlogic.deg.constraint.IConstraint;
import com.scottlogic.deg.constraint.NumericLimitConstConstraint;
import com.scottlogic.deg.constraint.TypeConstraint;
import com.scottlogic.deg.input.Field;
import com.scottlogic.deg.restriction.NumericFieldRestriction;
import com.scottlogic.deg.restriction.StringFieldRestriction;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class ConstraintReducerTest {
    private final ConstraintReducer constraintReducer = new ConstraintReducer();

    @Test
    public void test() {
        var quantity = new Field("quantity");
        var country = new Field("country");
        var city = new Field("city");

        var countryAmong = Set.of("UK", "US");

        final Iterable<IConstraint> constraints = List.of(
                new NumericLimitConstConstraint<>(quantity, 0, NumericLimitConstConstraint.LimitType.Min, Integer.class),
                new NumericLimitConstConstraint<>(quantity, 10, NumericLimitConstConstraint.LimitType.Max, Integer.class),
                new AmongConstraint<>(country, countryAmong, String.class),
                new TypeConstraint<>(city, String.class)
        );
        final var reducedConstraints = constraintReducer.getReducedConstraints(constraints);

        final var quantityRestriction = new NumericFieldRestriction<>(quantity, Integer.class);
        quantityRestriction.setMin(0);
        quantityRestriction.setMax(10);

        final var countryRestriction = new StringFieldRestriction(country);
        countryRestriction.setAmong(countryAmong);

        final var cityRestriction = new StringFieldRestriction(city);

//        final var expectation = List.of(
//                quantityRestriction,
//                countryRestriction,
//                cityRestriction
//        );
        assertThat(
                reducedConstraints,
                containsInAnyOrder(
                        Matchers.samePropertyValuesAs(quantityRestriction),
                        Matchers.samePropertyValuesAs(countryRestriction),
                        Matchers.samePropertyValuesAs(cityRestriction)
                )
        );
    }
}
