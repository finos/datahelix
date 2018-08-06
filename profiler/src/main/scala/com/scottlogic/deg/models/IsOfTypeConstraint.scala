package com.scottlogic.deg.models

class IsOfTypeConstraint(val FieldName : String, val Value : String) extends IConstraint {
  val Is : String = "ofType";
}