package com.scottlogic.deg.mappers

import com.scottlogic.deg.schemas.v3.FieldDTO
import com.scottlogic.deg.models.Field

object FieldMapper extends IMapper[FieldDTO,Field] {
  override def Map(original: FieldDTO): Field = new Field(original.name)
}
