package com.scottlogic.deg.models

class Rule() {
  var Description: String = null
  var Constraints: List[IConstraint] = null

  def this(description: String, constraints: List[IConstraint]) {
    this()
    this.Description = description
    this.Constraints = constraints
  }
}