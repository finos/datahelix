package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, FloatType, SemanticType}

object FloatClassifier extends Classifier {
  private val containsCharacterRegex = "[a-zA-Z]+"
  private val containsNumbersWithSeparator = "^[-]?\\d*[\\.,]\\d+$"

  override def classify(input: String): Seq[SemanticType] = {
    if (input.matches(containsCharacterRegex)) {
      Seq[SemanticType]()
    } else if (input.matches(containsNumbersWithSeparator)) {
      //TODO: Improve logic ( thousand separator, etc )
      Seq[SemanticType](FloatType)
    } else {
      Seq[SemanticType]()
    }
  }
}
