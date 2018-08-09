package com.scottlogic.deg.classifier

object CountryCodeClassifier extends Classifier {
  override def classify(input: String): Seq[SemanticType] = {
    // TODO: Classifier logic
    return Seq[SemanticType](CountryCodeType)
  }
}
