package com.scottlogic.deg.dto

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "kind"
)
@JsonSubTypes(Array(
  new Type(value = classOf[NumericField], name = "numeric"),
  new Type(value = classOf[StringField], name = "string"),
  new Type(value = classOf[UnknownField], name = "unknown")
))
trait AbstractField {
  def name: String
}