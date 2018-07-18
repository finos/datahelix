package com.scottlogic.deg.dto

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "kind"
)
@JsonSubTypes(Array(
  new Type(value = classOf[NormalDistribution], name = "normal"),
  new Type(value = classOf[SetDistribution], name = "set"),
  new Type(value = classOf[PerCharacterRandomDistribution], name = "perCharacterRandom")
))
trait AbstractDistribution