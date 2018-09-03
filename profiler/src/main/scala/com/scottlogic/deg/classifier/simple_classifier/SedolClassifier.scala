package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{SedolType, SemanticType}

object SedolClassifier extends Classifier {
  private val sedolRegex = "^[B-Db-dF-Hf-hJ-Nj-nP-Tp-tV-Xv-xYyZz\\d]{6}\\d$"
  override val semanticType: SemanticType = SedolType
  override def matches(input: String): Boolean = input.matches(sedolRegex)
}
