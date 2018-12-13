package com.scottlogic.deg.mappers

import com.scottlogic.deg.models.Profile
import com.scottlogic.deg.schemas.v3.V3ProfileDTO

import scala.collection.JavaConversions

object ProfileDTOMapper extends Mapper[Profile,V3ProfileDTO] {
  override def Map(original: Profile): V3ProfileDTO = {
    if (original == null) return null

    val profile = new V3ProfileDTO()
    profile.fields = JavaConversions.asJavaCollection(original.fields.map(field => FieldDTOMapper.Map(field)))
    profile.rules = JavaConversions.asJavaCollection(original.rules.map(rule => RuleDTOMapper.Map(rule)))
    profile
  }
}
