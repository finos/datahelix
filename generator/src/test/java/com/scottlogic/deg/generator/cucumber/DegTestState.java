package com.scottlogic.deg.generator.cucumber;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.ArrayList;
import java.util.List;

public class DegTestState {
    final List<Field> profileFields = new ArrayList<>();
    final List<IConstraint> constraints = new ArrayList<>();
}
