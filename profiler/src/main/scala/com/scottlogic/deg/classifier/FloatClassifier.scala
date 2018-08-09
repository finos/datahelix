package com.scottlogic.deg.classifier

object FloatClassifier extends Classifier {
  override def classify(input: String): Seq[SemanticType] = {
    // TODO: Classifier logic
    return Seq[SemanticType](FloatType)
  }
}
