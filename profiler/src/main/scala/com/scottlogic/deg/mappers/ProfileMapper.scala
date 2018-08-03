package com.scottlogic.deg.mappers

import com.scottlogic.deg.schemas.v3.V3ProfileDTO
import com.scottlogic.deg.models.Profile

import scala.collection.JavaConversions

object ProfileMapper extends IMapper[V3ProfileDTO,Profile] {
  override def Map(original: V3ProfileDTO): Profile = {
    if(original == null){
      return null;
    }

    val profile = new Profile();
    if(original.fields != null){
      profile.Fields = JavaConversions.asScalaIterator(original.fields.iterator()).map(fieldDTO => FieldMapper.Map(fieldDTO)).toList;
    }
    if(original.rules != null){
      profile.Rules = JavaConversions.asScalaIterator(original.rules.iterator()).map(ruleDTO => RuleMapper.Map(ruleDTO)).toList;
    }
    return profile;
  }
}
