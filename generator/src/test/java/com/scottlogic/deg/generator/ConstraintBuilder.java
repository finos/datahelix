package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.*;
import com.scottlogic.deg.generator.constraints.atomic.IsEqualToConstantConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConstraintBuilder {
    private final List<Constraint> constraints =  new ArrayList<>();
    private final Map<String, Field> fields;

    public ConstraintBuilder(List<Field> fields){
        this.fields = fields.stream().collect(Collectors.toMap(f -> f.name, f -> f));
    }

    public List<Constraint> build() {
        return constraints;
    }

    public ConstraintBuilder addInSetConstraint(String fieldname, List<Object> values){
        constraints.add(new IsInSetConstraint(fields.get(fieldname), new HashSet<>(values)));
        return this;
    }

    public ConstraintBuilder addEqualToConstraint(String fieldname, Object value){
        constraints.add(new IsEqualToConstantConstraint(fields.get(fieldname), value));
        return this;
    }

    public ConstraintBuilder addConditionalConstraint(List<Constraint> predicates, List<Constraint> consequences){
        constraints.add(new ConditionalConstraint(new AndConstraint(predicates), new AndConstraint(consequences)));
        return this;
    }

    public ConstraintBuilder addNullConstraint(String fieldName){
        constraints.add(new IsNullConstraint(fields.get(fieldName)));
        return this;
    }

}
