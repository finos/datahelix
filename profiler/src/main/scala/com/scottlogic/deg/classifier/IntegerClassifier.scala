package com.scottlogic.deg.classifier

object IntegerClassifier extends Classifier {
  override def classify(input: String): Seq[SemanticType] = {
    // TODO: Classifier logic
    return Seq[SemanticType](IntegerType)
  }
}
