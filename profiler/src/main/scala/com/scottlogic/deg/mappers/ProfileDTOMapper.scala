package com.scottlogic.deg.mappers

import com.scottlogic.deg.models.Profile
import com.scottlogic.deg.schemas.v3.V3ProfileDTO

import scala.collection.JavaConversions

object ProfileDTOMapper extends IMapper[Profile,V3ProfileDTO] {
  override def Map(original: Profile): V3ProfileDTO = {
    if(original == null){
      return null;
    }

    val profile = new V3ProfileDTO();
    if(original.Fields != null){
      profile.fields = JavaConversions.asJavaCollection(original.Fields.map(field => FieldDTOMapper.Map(field)));
    }
    if(original.Rules != null){
      profile.rules = JavaConversions.asJavaCollection(original.Rules.map(rule => RuleDTOMapper.Map(rule)));
    }
    return profile;
  }
}