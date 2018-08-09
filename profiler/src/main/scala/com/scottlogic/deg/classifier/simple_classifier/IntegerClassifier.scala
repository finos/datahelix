package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, IntegerType, SemanticType}

object IntegerClassifier extends Classifier {
  private val integerRegex : String = "^\\d+$";
  override def classify(input: String): Seq[SemanticType] = {
    if(input == null || input == ""){
      return List[SemanticType]()
    }

    if(input.matches(integerRegex)){
      return Seq[SemanticType](IntegerType)
    }

    return Seq[SemanticType]()
  }
}
