package com.scottlogic.deg.orchestrator.violate;

import com.scottlogic.deg.orchestrator.guice.AllConfigSource;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;

import java.util.List;

public interface ViolateConfigSource extends AllConfigSource {
    List<AtomicConstraintType> getConstraintsToNotViolate();
}
