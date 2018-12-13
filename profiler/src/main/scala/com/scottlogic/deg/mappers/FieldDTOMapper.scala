package com.scottlogic.deg.mappers

import com.scottlogic.deg.models.Field
import com.scottlogic.deg.schemas.v3.FieldDTO

object FieldDTOMapper extends Mapper[Field, FieldDTO] {
  override def Map(original: Field): FieldDTO = new FieldDTO(original.Name)
}
