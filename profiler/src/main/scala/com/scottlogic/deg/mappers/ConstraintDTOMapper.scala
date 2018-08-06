package com.scottlogic.deg.mappers

import com.scottlogic.deg.models._
import com.scottlogic.deg.schemas.v3.{ConstraintDTO, ConstraintDTOBuilder}

object ConstraintDTOMapper extends IMapper[IConstraint,ConstraintDTO] {
  override def Map(original: IConstraint): ConstraintDTO = {
    if(original == null){
      return null;
    }

    val builder = ConstraintDTOBuilder.instance;

    if(original.isInstanceOf[IsOfTypeConstraint]){
      val instance = original.asInstanceOf[IsOfTypeConstraint];
      return builder.appendField(instance.FieldName)
          .appendIs(instance.Is)
          .appendValue(instance.Value)
          .Build;
    }

    if(original.isInstanceOf[IsGreaterThanOrEqualToConstantConstraint]){
      val instance = original.asInstanceOf[IsGreaterThanOrEqualToConstantConstraint];
      return builder.appendField(instance.FieldName)
        .appendIs(instance.Is)
        .appendValue(instance.Value)
        .Build;
    }

    if(original.isInstanceOf[IsLowerThanConstraint]){
      val instance = original.asInstanceOf[IsLowerThanConstraint];
      return builder.appendField(instance.FieldName)
        .appendIs(instance.Is)
        .appendValue(instance.Value)
        .Build;
    }

    if(original.isInstanceOf[MatchesRegexConstraint]){
      val instance = original.asInstanceOf[MatchesRegexConstraint];
      return builder.appendField(instance.FieldName)
        .appendIs(instance.Is)
        .appendValue(instance.Value)
        .Build;
    }

    return builder.Build;
  }
}