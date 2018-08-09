package com.scottlogic.deg.classifier

object FloatClassifier extends Classifier {
  private val containsCharacterRegex = "[a-zA-Z]+"
  private val containsNumbersRegex = "\\d+";
  private val containsNumbersWithSeparator = "^\\d*[\\.,]?\\d+$"

  override def classify(input: String): Seq[SemanticType] = {
    if(input == null || input == ""){
      return List[SemanticType]()
    }

    if(input.matches(containsCharacterRegex)){
      return List[SemanticType]()
    }

    if(!input.matches(containsNumbersRegex)){
      return List[SemanticType]()
    }

    //TODO: Improve logic ( thousand separator, etc )
    if(input.matches(containsNumbersWithSeparator)){
      return List[SemanticType](FloatType)
    }

    return List[SemanticType]()
  }
}
