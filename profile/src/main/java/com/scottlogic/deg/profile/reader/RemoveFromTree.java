package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.generator.profile.constraints.Constraint;

public class RemoveFromTree implements Constraint {
    public RemoveFromTree() {
    }

    @Override
    public Constraint negate() {
        throw new UnsupportedOperationException();
    }
}
