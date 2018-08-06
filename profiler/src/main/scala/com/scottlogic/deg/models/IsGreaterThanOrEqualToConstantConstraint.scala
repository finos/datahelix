package com.scottlogic.deg.models

class IsGreaterThanOrEqualToConstantConstraint(val FieldName : String, val Value : String) extends IConstraint {
  val Is : String = "greaterThanOrEqual";
}