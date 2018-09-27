package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{IsinType, SemanticType}

object IsinClassifier extends Classifier {
  private val countryCodes = CountryCodeClassifier.Iso2CountryCodes.mkString("|")
  /*
   * ISIN: https://www.iso.org/standard/44811.html
   * 
   * "XS" is for codes obtained via an international central depository
   * c.f. https://www.isin.net/xs-isin/
   */
  private val isinRegex : String = "^(XS|"+countryCodes+")([0-9A-Z]{9})([0-9]{1})$";
  override val semanticType: SemanticType = IsinType
  override def matches(input: String): Boolean = input.matches(isinRegex)
}
