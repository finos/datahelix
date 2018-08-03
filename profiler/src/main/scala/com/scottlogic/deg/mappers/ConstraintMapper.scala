package com.scottlogic.deg.mappers

import com.scottlogic.deg.models.{Constraint, ConstraintBuilder}
import com.scottlogic.deg.schemas.v3.ConstraintDTO

import scala.collection.JavaConversions

object ConstraintMapper extends IMapper[ConstraintDTO,Constraint] {
  override def Map(original: ConstraintDTO): Constraint = {
    if(original == null) {
      return null;
    }

    var builder = ConstraintBuilder.instance
    .appendField(original.field)
    .appendElse(ConstraintMapper.Map(original.else_))
    .appendIf(ConstraintMapper.Map(original.if_))
    .appendThen(ConstraintMapper.Map(original.then_))
    .appendIs(original.is)
    .appendNot(ConstraintMapper.Map(original.not))
    .appendValue(original.value)

    if(original.allOf != null){
      builder = builder.appendAllOf(JavaConversions.asScalaIterator(original.allOf.iterator()).map(constraintDTO => ConstraintMapper.Map(constraintDTO)).toList);
    }
    if(original.anyOf != null){
      builder = builder.appendAnyOf(JavaConversions.asScalaIterator(original.anyOf.iterator()).map(constraintDTO => ConstraintMapper.Map(constraintDTO)).toList);
    }
    if(original.values != null){
      builder = builder.appendValues(JavaConversions.asScalaIterator(original.values.iterator()).toList);
    }

    return builder.Build;
  }
}