package com.scottlogic.deg.schemas.v3;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

class AtomicConstraintTypeTest {

    @Test
    void fromText() {
        String greaterThanString = AtomicConstraintType.ISGREATERTHANOREQUALTOCONSTANT.toString();
        AtomicConstraintType greaterThanOrEqualTo = AtomicConstraintType.fromText(greaterThanString);

        Assert.assertThat(greaterThanOrEqualTo, is(AtomicConstraintType.ISGREATERTHANOREQUALTOCONSTANT));
    }

    @Test
    void fromTextLowerCase() {
        AtomicConstraintType greaterThanOrEqualTo = AtomicConstraintType.fromText("shorterthan");

        Assert.assertThat(greaterThanOrEqualTo, is(AtomicConstraintType.ISSTRINGSHORTERTHAN));
    }
}