package com.scottlogic.deg.models

class Constraint {
  var Is: String = null
  var Field: String = null
  var Value: Any = null
  var Values: List[AnyRef] = null
  var Not: Constraint = null
  var AnyOf: List[Constraint] = null
  var AllOf: List[Constraint] = null
  var If: Constraint = null
  var Then: Constraint = null
  var Else: Constraint = null
}