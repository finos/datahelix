package com.scottlogic.deg.mappers

import com.scottlogic.deg.models._
import com.scottlogic.deg.schemas.v3.{AtomicConstraintType, ConstraintDTO, ConstraintDTOBuilder}

object ConstraintDTOMapper extends IMapper[IConstraint,ConstraintDTO] {
  override def Map(original: IConstraint): ConstraintDTO = {
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
      case instance: MatchesRegexConstraint =>
        builder.appendField(instance.FieldName)
          .appendIs(AtomicConstraintType.MATCHESREGEX.toString)
          .appendValue(instance.Value)
          .Build;
      // FIXME for now we can only generate granular-to constraint with a value of "1"
      case instance: GranularToConstraint if instance.Value==1.0 =>        
        builder.appendField(instance.FieldName)
          .appendIs(AtomicConstraintType.ISGRANULARTO.toString)
          .appendValue("1")
          .Build;
      case instance: GranularToConstraint =>        
        builder.appendField(instance.FieldName)
          .appendIs(AtomicConstraintType.ISGRANULARTO.toString)
          .appendValue(instance.Value)
          .Build;
      case _ =>
        throw new IllegalArgumentException("Can't convert constraint of supplied type")
    }
  }
}