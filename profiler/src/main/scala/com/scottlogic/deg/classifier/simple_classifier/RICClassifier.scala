package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, RICType, SemanticType}

object RICClassifier extends Classifier {
  private val ricRegex = "[A-Z]{1,4}\\.[A-Z]{1,2}";

  override def classify(input: String): Seq[SemanticType] = {
    // TODO: Classifier logic
    if(input == null || input == ""){
      return List[SemanticType]()
    }

    if(input.length < 3){
      return List[SemanticType]()
    }

    if(input.toUpperCase.matches(ricRegex)){
      return List[SemanticType](RICType)
    }

    return Seq[SemanticType]()
  }
}
