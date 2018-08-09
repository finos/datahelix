package com.scottlogic.deg.classifier

object EmailClassifier extends Classifier {
  override def classify(input: String): Seq[SemanticType] = {
    // TODO: Classifier logic
    return Seq[SemanticType](EmailType)
  }
}
