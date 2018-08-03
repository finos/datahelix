package com.scottlogic.deg.generator.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.*;
import com.scottlogic.deg.generator.restrictions.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

public class ConstraintReducerTest {
    @Test
    public void shouldProduceCorrectFieldSpecsForExample() {
        // ARRANGE
        final Field quantityField = new Field("quantity");
        final Field countryField = new Field("country");
        final Field cityField = new Field("city");

        ProfileFields fieldList = new ProfileFields(
            Arrays.asList(quantityField, countryField, cityField));

        final Set<Object> countryAmong = new HashSet<>(Arrays.asList("UK", "US"));

        final List<IConstraint> constraints = Arrays.asList(
                new IsGreaterThanConstantConstraint(quantityField, 0),
                new NotConstraint(new IsGreaterThanConstantConstraint(quantityField, 5)),
                new IsInSetConstraint(countryField, countryAmong),
                new IsOfTypeConstraint(cityField, IsOfTypeConstraint.Types.String));

        // ACT
        final RowSpec reducedConstraints = new ConstraintReducer().reduceConstraintsToRowSpec(
            fieldList,
            constraints);

        // ASSERT
        FieldSpec quantityFieldSpec = reducedConstraints.getSpecForField(quantityField);
        Assert.assertNull(quantityFieldSpec.getSetRestrictions());
        Assert.assertNull(quantityFieldSpec.getStringRestrictions());
        Assert.assertNull(quantityFieldSpec.getNullRestrictions());
        Assert.assertNull(quantityFieldSpec.getTypeRestrictions());
        Assert.assertNotNull(quantityFieldSpec.getNumericRestrictions());
        Assert.assertEquals(BigDecimal.ZERO, quantityFieldSpec.getNumericRestrictions().min.getLimit());
        Assert.assertFalse(quantityFieldSpec.getNumericRestrictions().min.isInclusive());
        Assert.assertEquals(BigDecimal.valueOf(5), quantityFieldSpec.getNumericRestrictions().max.getLimit());
        Assert.assertTrue(quantityFieldSpec.getNumericRestrictions().max.isInclusive());

        FieldSpec countryFieldSpec = reducedConstraints.getSpecForField(countryField);
        Assert.assertNull(countryFieldSpec.getStringRestrictions());
        Assert.assertNull(countryFieldSpec.getNullRestrictions());
        Assert.assertNull(countryFieldSpec.getTypeRestrictions());
        Assert.assertNull(countryFieldSpec.getNumericRestrictions());
        Assert.assertNotNull(countryFieldSpec.getSetRestrictions());
        Assert.assertNull(countryFieldSpec.getSetRestrictions().blacklist);
        Assert.assertNotNull(countryFieldSpec.getSetRestrictions().whitelist);
        Assert.assertEquals(2, countryFieldSpec.getSetRestrictions().whitelist.size());
        Assert.assertTrue(countryFieldSpec.getSetRestrictions().whitelist.contains("UK"));
        Assert.assertTrue(countryFieldSpec.getSetRestrictions().whitelist.contains("US"));

        FieldSpec cityFieldSpec = reducedConstraints.getSpecForField(cityField);
        Assert.assertNull(cityFieldSpec.getSetRestrictions());
        Assert.assertNull(cityFieldSpec.getStringRestrictions());
        Assert.assertNull(cityFieldSpec.getNullRestrictions());
        Assert.assertNull(cityFieldSpec.getNumericRestrictions());
        Assert.assertNotNull(cityFieldSpec.getTypeRestrictions());
        Assert.assertEquals(IsOfTypeConstraint.Types.String, cityFieldSpec.getTypeRestrictions().type);
    }
}
