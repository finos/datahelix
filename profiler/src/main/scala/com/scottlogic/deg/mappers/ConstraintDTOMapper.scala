package com.scottlogic.deg.mappers

import com.scottlogic.deg.models.Constraint
import com.scottlogic.deg.schemas.v3.{ConstraintDTO, ConstraintDTOBuilder}

import scala.collection.JavaConversions

object ConstraintDTOMapper extends IMapper[Constraint,ConstraintDTO] {
  override def Map(original: Constraint): ConstraintDTO = {
    if(original == null){
      return null;
    }

    var builder = ConstraintDTOBuilder.instance()
    .appendField(original.Field)
    .appendElse(ConstraintDTOMapper.Map(original.Else))
    .appendIf(ConstraintDTOMapper.Map(original.If))
    .appendThen(ConstraintDTOMapper.Map(original.Then))
    .appendIs(original.Is)
    .appendNot(ConstraintDTOMapper.Map(original.Not))
    .appendValue(original.Value);

    if(original.AllOf != null){
      builder = builder.appendAllOf(JavaConversions.asJavaCollection(original.AllOf.map(constraint => ConstraintDTOMapper.Map(constraint))));
    }
    if(original.AnyOf != null){
      builder = builder.appendAnyOf(JavaConversions.asJavaCollection(original.AnyOf.map(constraint => ConstraintDTOMapper.Map(constraint))));
    }
    if(original.Values != null){
      builder = builder.appendValues(JavaConversions.asJavaCollection(original.Values));
    }

    return builder.Build;
  }
}