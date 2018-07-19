package com.scottlogic.deg.schemas.v2.constraints;

import java.util.ArrayList;
import java.util.List;

public class EnumConstraint extends Constraint {
    public List<String> members = new ArrayList<String>();

    public EnumConstraint() {
        super(ConstraintTypes.EnumMembers);
    }
}
