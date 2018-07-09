package com.scottlogic.deg.analyser.field_analyser

import com.scottlogic.deg.dto.AbstractField

trait FieldAnalyser extends {
    def constructDTOField():AbstractField
}
