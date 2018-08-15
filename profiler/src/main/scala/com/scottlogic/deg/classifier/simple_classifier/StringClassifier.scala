package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, SemanticType, StringType}

object StringClassifier extends Classifier {
  override def classify(input: String): Seq[SemanticType] = {
    // TODO: Classifier logic

    return Seq[SemanticType](StringType)
  }
}
