package com.scottlogic.deg.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.*;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.restrictions.NumericRestrictions.NumericLimit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class ConstraintReducerTest {
    private final ConstraintReducer constraintReducer = new ConstraintReducer();

    @Test
    public void test() {
        final Field quantity = new Field("quantity");
        final Field country = new Field("country");
        final Field city = new Field("city");

        final Set<Object> countryAmong = new HashSet<>(Arrays.asList("UK", "US"));

        final List<IConstraint> constraints = Arrays.asList(
                new IsGreaterThanConstantConstraint(quantity, 0),
                new NotConstraint(new IsGreaterThanConstantConstraint(quantity, 5)),
                new IsInSetConstraint(country, countryAmong),
                new IsOfTypeConstraint(city, IsOfTypeConstraint.Types.String)
        );
        final RowSpec reducedConstraints = constraintReducer.getReducedConstraints(constraints);

        final FieldSpec quantitySpec = new FieldSpec(quantity.name);
        final FieldSpec countrySpec = new FieldSpec(country.name);
        final FieldSpec citySpec = new FieldSpec(city.name);

        final NumericRestrictions quantityNumericRestriction = new NumericRestrictions();
        quantityNumericRestriction.min = new NumericLimit(
                BigDecimal.valueOf(0),
                false
        );
        quantityNumericRestriction.max = new NumericLimit(
                BigDecimal.valueOf(5),
                true
        );
        quantitySpec.setNumericRestrictions(quantityNumericRestriction);

        final SetRestrictions countrySetRestriction = new SetRestrictions();
        countrySetRestriction.whitelist = countryAmong;
        countrySpec.setSetRestrictions(countrySetRestriction);

        final TypeRestrictions cityTypeRestriction = new TypeRestrictions();
        citySpec.setTypeRestrictions(cityTypeRestriction);

        assertThat(
                reducedConstraints.getFieldSpecs(),
                containsInAnyOrder(
                        Matchers.samePropertyValuesAs(quantitySpec),
                        Matchers.samePropertyValuesAs(countrySpec),
                        Matchers.samePropertyValuesAs(citySpec)
                )
        );
    }
}
