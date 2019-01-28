package com.scottlogic.deg.generator.violations.filters;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.IsGreaterThanConstantConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;

class ConstraintTypeFilterTest {

    @Test
    void inSetConstraintTypeFilter_doesNotAccept_inSetConstraint() {
        ViolationFilter violationFilter = new ConstraintTypeViolationFilter(IsInSetConstraint.class);
        Constraint inSet = new IsInSetConstraint(null, Collections.singleton(""), null);

        Assert.assertThat(violationFilter.accept(inSet), is(false));
    }

    @Test
    void inSetConstraintTypeFilter_doesNotAccept_ifSomethingThenInSetConstraint() {
        ViolationFilter violationFilter = new ConstraintTypeViolationFilter(IsInSetConstraint.class);
        Constraint inSet = new IsInSetConstraint(null, Collections.singleton(""), null);
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(null, inSet);

        Assert.assertThat(violationFilter.accept(conditionalConstraint), is(false));
    }

    @Test
    void inSetConstraintTypeFilter_doesNotAccept_ifSomethingThenAllOfInSetConstraint() {
        ViolationFilter violationFilter = new ConstraintTypeViolationFilter(IsInSetConstraint.class);
        Constraint inSet = new IsInSetConstraint(null, Collections.singleton(""), null);
        AndConstraint andConstraint = new AndConstraint(inSet);
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(null, andConstraint);

        Assert.assertThat(violationFilter.accept(conditionalConstraint), is(false));
    }

    @Test
    void greaterThanConstraintTypeFilter_accepts_inSetConstraint() {
        ViolationFilter violationFilter = new ConstraintTypeViolationFilter(IsGreaterThanConstantConstraint.class);
        Constraint inSet = new IsInSetConstraint(null, Collections.singleton(""), null);

        Assert.assertThat(violationFilter.accept(inSet), is(true));
    }

    @Test
    void greaterThanConstraintTypeFilter_accepts_ifSomethingThenInSetConstraint() {
        ViolationFilter violationFilter = new ConstraintTypeViolationFilter(IsGreaterThanConstantConstraint.class);
        Constraint inSet = new IsInSetConstraint(null, Collections.singleton(""), null);
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(null, inSet);

        Assert.assertThat(violationFilter.accept(conditionalConstraint), is(true));
    }

    @Test
    void greaterThanConstraintTypeFilter_accepts_ifSomethingThenAllOfInSetConstraint() {
        ViolationFilter violationFilter = new ConstraintTypeViolationFilter(IsGreaterThanConstantConstraint.class);
        Constraint inSet = new IsInSetConstraint(null, Collections.singleton(""), null);
        AndConstraint andConstraint = new AndConstraint(inSet);
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(null, andConstraint);

        Assert.assertThat(violationFilter.accept(conditionalConstraint), is(true));
    }
}