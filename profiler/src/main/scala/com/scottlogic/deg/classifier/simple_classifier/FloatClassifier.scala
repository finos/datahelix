package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{FloatType, SemanticType}

object FloatClassifier extends Classifier {
  private val containsCharacterRegex = "[a-zA-Z]+"
  private val containsNumbersWithSeparator = "^[-]?\\d*[\\.,]?\\d+$"
  override val semanticType: SemanticType = FloatType

  override def matches(input: String): Boolean = {
    !input.matches(containsCharacterRegex) &&
      input.matches(containsNumbersWithSeparator)
  }
}
