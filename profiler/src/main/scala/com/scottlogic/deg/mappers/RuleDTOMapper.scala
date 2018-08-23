package com.scottlogic.deg.mappers

import com.scottlogic.deg.models.Rule
import com.scottlogic.deg.schemas.v3.RuleDTO

import scala.collection.JavaConversions

object RuleDTOMapper extends IMapper[Rule,RuleDTO] {
  override def Map(original: Rule): RuleDTO = new RuleDTO(original.Description, JavaConversions.asJavaCollection(original.Constraints.map(constraint => ConstraintDTOMapper.Map(constraint))))
}
