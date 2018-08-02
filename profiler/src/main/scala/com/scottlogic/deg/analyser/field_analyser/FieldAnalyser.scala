package com.scottlogic.deg.analyser.field_analyser

import com.scottlogic.deg.schemas.v3
import com.scottlogic.deg.schemas.v3.RuleDTO

trait FieldAnalyser extends {
    def constructDTOField():RuleDTO
}
