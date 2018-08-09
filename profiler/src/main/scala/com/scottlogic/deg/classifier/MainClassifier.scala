package com.scottlogic.deg.classifier

import scala.collection.mutable.ListBuffer

object MainClassifier extends Classifier {
  private val classifiers : List[Classifier] = List[Classifier](CountryCodeClassifier, CurrencyClassifier,EmailClassifier, FloatClassifier, IntegerClassifier, NameClassifier, RICClassifier, ISINClassifier, SEDOLClassifier, StringClassifier, TimeStampClassifier)

  override def classify(input: String): Seq[SemanticType] = {
    val results : ListBuffer[SemanticType] = ListBuffer[SemanticType]()
    classifiers.foreach(classifier => results.appendAll(classifier.classify(input)))
    return results.toList
  }
}