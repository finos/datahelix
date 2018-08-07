package com.scottlogic.deg.mappers

import com.scottlogic.deg.models._
import com.scottlogic.deg.schemas.v3.{ConstraintDTO, ConstraintDTOBuilder}

object ConstraintDTOMapper extends IMapper[IConstraint,ConstraintDTO] {
  override def Map(original: IConstraint): ConstraintDTO = {
    if(original == null){
      throw new IllegalArgumentException("constraint argument was null")
    }

    val builder = ConstraintDTOBuilder.instance;

    original match {
      case instance: IsOfTypeConstraint =>
        builder.appendField(instance.FieldName)
          .appendIs("ofType")
          .appendValue(instance.Value)
          .Build;
      case instance: IsGreaterThanOrEqualToConstantConstraint =>
        builder.appendField(instance.FieldName)
          .appendIs("greaterThanOrEqual")
          .appendValue(instance.Value)
          .Build;
      case instance: IsLowerThanConstraint =>
        builder.appendField(instance.FieldName)
          .appendIs("lowerThan")
          .appendValue(instance.Value)
          .Build;
      case instance: MatchesRegexConstraint =>
        builder.appendField(instance.FieldName)
          .appendIs("matchesRegex")
          .appendValue(instance.Value)
          .Build;
      case instance: HasDecimalPlacesConstraint =>
        builder.appendField(instance.FieldName)
          .appendIs("hasDecimalPlaces")
          .appendValue(instance.NumberOfDecimalPlaces)
          .Build;
      case _ =>
        throw new IllegalArgumentException("Can't convert constraint of supplied type")
    }
  }
}