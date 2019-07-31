package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.constraints.delayed.DelayedAtomicConstraint;
import com.scottlogic.deg.common.profile.constraints.delayed.DynamicNotConstraint;
import com.scottlogic.deg.common.profile.constraints.delayed.IsAfterDynamicDateTimeConstraint;
import com.scottlogic.deg.generator.fieldspecs.relations.AfterDateRelation;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;

public class FieldRelationsFactory {

   public FieldSpecRelations construct(DelayedAtomicConstraint constraint) {
       return construct(constraint, false);
   }

   private FieldSpecRelations construct(DelayedAtomicConstraint constraint, boolean negate) {
       if (constraint instanceof DynamicNotConstraint) {
           return construct((DynamicNotConstraint) constraint.negate(), !negate);
       } else if (constraint instanceof IsAfterDynamicDateTimeConstraint) {
           return constructAfterDate((IsAfterDynamicDateTimeConstraint) constraint);
       } else {
           throw new IllegalArgumentException("Unsupported field spec relations");
       }
   }

   private FieldSpecRelations constructAfterDate(IsAfterDynamicDateTimeConstraint constraint) {
       return new AfterDateRelation(constraint.underlyingConstraint().getField(), constraint.field());
   }
}
