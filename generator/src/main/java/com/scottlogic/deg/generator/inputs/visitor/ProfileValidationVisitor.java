package com.scottlogic.deg.generator.inputs.visitor;

import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileValidationVisitor implements IProfileVisitor {

    @Override
    public void visit(Collection<Rule> rules) {
        List<IConstraint> constraints = rules.stream()
            .flatMap(r -> r.constraints.stream())
            .collect(Collectors.toList());

        ConstraintValidationVisitor constraintValidationVisitor = new ConstraintValidationVisitor();

        List<ValidationAlert> alerts = new ArrayList<>();
        for(IConstraint constraint : constraints) {
            alerts.addAll(constraint.accept(constraintValidationVisitor));

        }

        if(alerts.size()>0) {
            boolean hasErrors = false;
            for(ValidationAlert alert : alerts) {

                if(alert.getCriticality().equals(ValidationAlert.Criticality.ERROR)){
                    hasErrors = true;
                }

                System.out.println(alert.toString());
            }

            if(hasErrors) {
                System.out.println("Encountered unrecoverable profile validation errors.");
                System.exit(1);
            }
        }
    }
}
