package com.scottlogic.deg.models

class Rule() {
  var Description: String = null
  var Constraints: List[Constraint] = null

  def this(description: String, constraints: List[Constraint]) {
    this()
    this.Description = description
    this.Constraints = constraints
  }
}