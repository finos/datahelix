package com.scottlogic.deg.classifier

object StringClassifier extends Classifier {
  override def classify(input: String): Seq[SemanticType] = {
    // TODO: Classifier logic
    return Seq[SemanticType](StringType)
  }
}
