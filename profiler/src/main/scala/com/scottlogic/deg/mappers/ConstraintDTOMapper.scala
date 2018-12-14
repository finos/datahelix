package com.scottlogic.deg.mappers

import com.scottlogic.deg.models._
import com.scottlogic.deg.schemas.v3.{AtomicConstraintType, ConstraintDTO, ConstraintDTOBuilder}

import collection.JavaConverters._

object ConstraintDTOMapper extends Mapper[Constraint,ConstraintDTO] {
  override def Map(original: Constraint): ConstraintDTO = {
    if(original == null){
      throw new IllegalArgumentException("constraint argument was null")
    }

    val builder = ConstraintDTOBuilder.instance;

    original match {
      case instance: IsAValidConstraint =>
        builder.appendField(instance.FieldName)
          .appendIs(AtomicConstraintType.AVALID.toString)
          .appendValue(instance.Value)
          .Build;
      case instance: IsOfTypeConstraint =>
        builder.appendField(instance.FieldName)
          .appendIs(AtomicConstraintType.ISOFTYPE.toString)
          .appendValue(instance.Value)
          .Build;
      case instance: IsGreaterThanOrEqualToConstantConstraint =>
        builder.appendField(instance.FieldName)
          .appendIs(AtomicConstraintType.ISGREATERTHANOREQUALTOCONSTANT.toString)
          .appendValue(instance.Value)
          .Build;
      case instance: IsLowerThanConstraint =>
        builder.appendField(instance.FieldName)
          .appendIs(AtomicConstraintType.ISLESSTHANCONSTANT.toString)
          .appendValue(instance.Value)
          .Build;
      case instance: IsAfterOrEqualToConstantDateTimeConstraint =>
        builder.appendField(instance.FieldName)
          .appendIs(AtomicConstraintType.ISAFTEROREQUALTOCONSTANTDATETIME.toString)
          .appendValue(instance.Value)
          .Build;
      case instance: IsBeforeConstantDateTimeConstraint =>
        builder.appendField(instance.FieldName)
          .appendIs(AtomicConstraintType.ISBEFORECONSTANTDATETIME.toString)
          .appendValue(instance.Value)
          .Build;
      case instance: MatchesRegexConstraint =>
        builder.appendField(instance.FieldName)
          .appendIs(AtomicConstraintType.MATCHESREGEX.toString)
          .appendValue(instance.Value)
          .Build;
      case instance: GranularToConstraint => 
        builder.appendField(instance.FieldName)
          .appendIs(AtomicConstraintType.ISGRANULARTO.toString)
          .appendValue(instance.Value)
          .Build;
      case instance: InSetConstraint => 
        builder.appendField(instance.FieldName)
          .appendIs(AtomicConstraintType.ISINSET.toString)
          .appendValues(instance.Value.asJava)
          .Build;
      case _ =>
        throw new IllegalArgumentException("Can't convert constraint of supplied type")
    }
  }
}