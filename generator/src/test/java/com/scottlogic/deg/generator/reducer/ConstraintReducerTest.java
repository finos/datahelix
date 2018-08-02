package com.scottlogic.deg.generator.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.*;
import com.scottlogic.deg.generator.restrictions.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

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



        ArrayList<String> seenFields = new ArrayList<>();
        for (FieldSpec fieldSpec : reducedConstraints.getFieldSpecs()) {
            Assert.assertTrue(Arrays.asList("quantity", "country", "city").contains(fieldSpec.getName()));
            Assert.assertFalse(seenFields.contains(fieldSpec.getName()));
            seenFields.add(fieldSpec.getName());
            if (fieldSpec.getName().equals("quantity")) {
                Assert.assertNull(fieldSpec.getSetRestrictions());
                Assert.assertNull(fieldSpec.getStringRestrictions());
                Assert.assertNull(fieldSpec.getNullRestrictions());
                Assert.assertNull(fieldSpec.getTypeRestrictions());
                Assert.assertNotNull(fieldSpec.getNumericRestrictions());
                Assert.assertEquals(BigDecimal.ZERO, fieldSpec.getNumericRestrictions().min.getLimit());
                Assert.assertFalse(fieldSpec.getNumericRestrictions().min.isInclusive());
                Assert.assertEquals(BigDecimal.valueOf(5), fieldSpec.getNumericRestrictions().max.getLimit());
                Assert.assertTrue(fieldSpec.getNumericRestrictions().max.isInclusive());
            }
            else if (fieldSpec.getName().equals("country")) {
                Assert.assertNull(fieldSpec.getStringRestrictions());
                Assert.assertNull(fieldSpec.getNullRestrictions());
                Assert.assertNull(fieldSpec.getTypeRestrictions());
                Assert.assertNull(fieldSpec.getNumericRestrictions());
                Assert.assertNotNull(fieldSpec.getSetRestrictions());
                Assert.assertNull(fieldSpec.getSetRestrictions().blacklist);
                Assert.assertNotNull(fieldSpec.getSetRestrictions().whitelist);
                Assert.assertEquals(2, fieldSpec.getSetRestrictions().whitelist.size());
                Assert.assertTrue(fieldSpec.getSetRestrictions().whitelist.contains("UK"));
                Assert.assertTrue(fieldSpec.getSetRestrictions().whitelist.contains("US"));
            }
            else if (fieldSpec.getName().equals("city")) {
                Assert.assertNull(fieldSpec.getSetRestrictions());
                Assert.assertNull(fieldSpec.getStringRestrictions());
                Assert.assertNull(fieldSpec.getNullRestrictions());
                Assert.assertNull(fieldSpec.getNumericRestrictions());
                Assert.assertNotNull(fieldSpec.getTypeRestrictions());
                Assert.assertEquals(IsOfTypeConstraint.Types.String, fieldSpec.getTypeRestrictions().type);
            }
        }
    }
}
