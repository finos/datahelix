package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.constraints.delayed.DelayedAtomicConstraint;
import com.scottlogic.deg.common.profile.constraints.delayed.DynamicNotConstraint;
import com.scottlogic.deg.common.profile.constraints.delayed.IsAfterDynamicDateTimeConstraint;
import com.scottlogic.deg.common.profile.constraints.delayed.IsEqualToDynamicDateConstraint;
import com.scottlogic.deg.generator.fieldspecs.relations.AfterDateRelation;
import com.scottlogic.deg.generator.fieldspecs.relations.EqualToDateRelation;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;

public class FieldRelationsFactory {

   public FieldSpecRelations construct(DelayedAtomicConstraint constraint) {
       return construct(constraint, false);
   }

   private FieldSpecRelations construct(DelayedAtomicConstraint constraint, boolean negate) {
       if (constraint instanceof DynamicNotConstraint) {
           return construct(constraint.negate(), !negate);
       } else if (constraint instanceof IsAfterDynamicDateTimeConstraint) {
           return constructAfterDate((IsAfterDynamicDateTimeConstraint) constraint);
       } else if (constraint instanceof IsEqualToDynamicDateConstraint) {
            return constructEqualToDate((IsEqualToDynamicDateConstraint) constraint);
       } else {
           throw new IllegalArgumentException("Unsupported field spec relations: " + constraint.getClass());
       }
   }

   private FieldSpecRelations constructAfterDate(IsAfterDynamicDateTimeConstraint constraint) {
       return new AfterDateRelation(constraint.underlyingConstraint().getField(), constraint.field());
   }

   private FieldSpecRelations constructEqualToDate(IsEqualToDynamicDateConstraint constraint) {
       return new EqualToDateRelation(constraint.underlyingConstraint().getField(), constraint.field());
   }
}
