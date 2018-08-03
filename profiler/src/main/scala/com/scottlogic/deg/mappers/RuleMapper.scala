package com.scottlogic.deg.mappers

import com.scottlogic.deg.schemas.v3.RuleDTO
import com.scottlogic.deg.models.{Rule}

import scala.collection.JavaConversions

object RuleMapper extends IMapper[RuleDTO,Rule] {
  override def Map(original: RuleDTO): Rule = {
    val constraintList = JavaConversions.asScalaIterator(original.constraints.iterator()).map(constraintDTO => ConstraintMapper.Map(constraintDTO)).toList;
    return new Rule(original.description, constraintList);
  }
}