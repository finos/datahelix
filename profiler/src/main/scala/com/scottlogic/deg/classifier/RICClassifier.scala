package com.scottlogic.deg.classifier

object RICClassifier extends Classifier {
  override def classify(input: String): Seq[SemanticType] = {
    // TODO: Classifier logic
    return Seq[SemanticType](RICType)
  }
}
