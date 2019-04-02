package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Set;
import java.util.stream.Collectors;

public class CellSource {
    public final Field field;
    private final FieldSpecSource source;

    public CellSource(DataBagValue value, Field field) {
        this.field = field;
        this.source = value.source != null ? value.source : FieldSpecSource.Empty;
    }

    public Set<AtomicConstraint> getConstraints(){
        return this.source.getConstraints();
    }

    public boolean isViolated(AtomicConstraint constraint){
        return this.source.getViolatedConstraints().contains(constraint);
    }

    public boolean isViolated(RuleInformation rule){
        return FlatMappingSpliterator.flatMap(this.source.getViolatedConstraints()
            .stream(), c -> c.getRules().stream())
            .anyMatch(r -> r.equals(rule));
    }

    public Set<RuleInformation> getRules(){
        return FlatMappingSpliterator.flatMap(this.source.getConstraints()
            .stream(), c -> c.getRules().stream())
            .collect(Collectors.toSet());
    }
}
