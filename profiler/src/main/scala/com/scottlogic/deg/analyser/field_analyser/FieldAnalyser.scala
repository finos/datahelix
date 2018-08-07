package com.scottlogic.deg.analyser.field_analyser

import com.scottlogic.deg.models.Rule

trait FieldAnalyser extends {
    def constructField():Rule
}
