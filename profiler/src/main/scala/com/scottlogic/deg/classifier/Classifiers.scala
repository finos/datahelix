package com.scottlogic.deg.classifier

import com.scottlogic.deg.classifier.simple_classifier._
import org.apache.spark.rdd.RDD

object Classifiers {
  val classifiers: List[Classifier] = List(
    CountryCodeClassifier, CurrencyClassifier,EmailClassifier, FloatClassifier, IntegerClassifier, NameClassifier,
    RicClassifier, IsinClassifier, SedolClassifier, StringClassifier, TimeStampClassifier
  )

  def classify(input: String): Set[SemanticType] = {
    if (input == null) {
      Set(NullType)
    } else {
      classifiers.flatMap(_.classify(input)).toSet
    }
  }
}
