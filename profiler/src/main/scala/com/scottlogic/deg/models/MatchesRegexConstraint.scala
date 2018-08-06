package com.scottlogic.deg.models

class MatchesRegexConstraint(val FieldName : String, val Value : String) extends IConstraint {
  val Is : String = "matchesRegex";
}