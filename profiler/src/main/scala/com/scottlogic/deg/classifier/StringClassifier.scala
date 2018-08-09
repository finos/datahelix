package com.scottlogic.deg.classifier

object StringClassifier extends Classifier {
  override def classify(input: String): Seq[SemanticType] = {
    // Fields are always strings when null or empty
    if(input == null || input == ""){
      return List[SemanticType](StringType)
    }

    // TODO: Classifier logic

    return Seq[SemanticType](StringType)
  }
}
