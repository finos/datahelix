package com.scottlogic.deg.models

class Rule() {
  var rule: String = null
  var Constraints: List[IConstraint] = null

  def this(description: String, constraints: List[IConstraint]) {
    this()
    this.rule = description
    this.Constraints = constraints
  }
}