package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, SemanticType, StringType}

object StringClassifier extends Classifier {
  override def classify(input: String): Set[SemanticType] = {
    // TODO: Classifier logic
    Set(StringType)
  }
}
