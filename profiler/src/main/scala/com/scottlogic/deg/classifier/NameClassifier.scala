package com.scottlogic.deg.classifier

object NameClassifier extends Classifier {
  override def classify(input: String): Seq[SemanticType] = {
    // TODO: Classifier logic
    if(input == null || input == ""){
      return List[SemanticType]()
    }
    return Seq[SemanticType]()
  }
}