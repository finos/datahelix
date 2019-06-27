package com.scottlogic.deg.profile.v0_1;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;

class AtomicConstraintTypeTest {

    @Test
    void fromText() {
        String greaterThanString = AtomicConstraintType.IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT.getText();
        AtomicConstraintType greaterThanOrEqualTo = AtomicConstraintType.fromText(greaterThanString);

        Assert.assertThat(greaterThanOrEqualTo, is(AtomicConstraintType.IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT));
    }

    @Test
    void fromTextLowerCase() {
        AtomicConstraintType greaterThanOrEqualTo = AtomicConstraintType.fromText("shorterthan");

        Assert.assertThat(greaterThanOrEqualTo, is(AtomicConstraintType.IS_STRING_SHORTER_THAN));
    }
}