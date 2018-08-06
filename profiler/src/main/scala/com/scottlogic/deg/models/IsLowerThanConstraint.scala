package com.scottlogic.deg.models

class IsLowerThanConstraint(val FieldName : String, val Value : String) extends IConstraint {
  val Is : String = "lowerThan";
}