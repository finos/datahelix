package com.scottlogic.deg.dto

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "kind"
)
@JsonSubTypes(Array(
  new Type(value = classOf[StringFieldEnumSpecialization], name = "enum"),
  new Type(value = classOf[StringFieldTextSpecialization], name = "text"),
  new Type(value = classOf[StringFieldUnknownSpecialization], name = "unknown")
))
trait StringFieldAbstractSpecialization