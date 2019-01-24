package com.scottlogic.deg.generator.violations.filters;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.IsStringShorterThanConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;

public class NotShorterThanFilter implements ViolationFilter {
    @Override
    public boolean accept(Constraint constraint) {
       if (constraint instanceof IsStringShorterThanConstraint) {
           return false;
       }
       if (constraint instanceof ConditionalConstraint){
           if (((ConditionalConstraint) constraint).whenConditionIsTrue instanceof IsStringShorterThanConstraint){
               return false;
           }
           if (((ConditionalConstraint) constraint).whenConditionIsTrue instanceof AndConstraint){
               for (Constraint subConstraint:((AndConstraint)((ConditionalConstraint) constraint).whenConditionIsTrue).subConstraints) {
                   if (subConstraint instanceof IsStringShorterThanConstraint){
                       return false;
                   }
               }
           }
       }
       return true;
    }
}
