package com.scottlogic.deg.models

object ConstraintBuilder {
  def instance = new ConstraintBuilder;
}

class ConstraintBuilder private() {

  private var constraint : Constraint = new Constraint;

  def appendField(fieldName: String): ConstraintBuilder = {
    this.constraint.Field = fieldName;
    return this;
  }

  def appendIs(constraintName: String): ConstraintBuilder = {
    this.constraint.Is = constraintName;
    return this;
  }

  def appendValue(value: Any): ConstraintBuilder = {
    this.constraint.Value = value;
    return this;
  }

  def appendValues(values: List[AnyRef]): ConstraintBuilder = {
    this.constraint.Values = values;
    return this;
  }

  def appendNot(constraint: Constraint): ConstraintBuilder = {
    this.constraint.Not = constraint;
    return this;
  }

  def appendAnyOf(constraints: List[Constraint]): ConstraintBuilder = {
    this.constraint.AnyOf = constraints;
    return this;
  }

  def appendAllOf(constraints: List[Constraint]): ConstraintBuilder = {
    this.constraint.AllOf = constraints;
    return this;
  }

  def appendIf(constraint: Constraint): ConstraintBuilder = {
    this.constraint.If = constraint;
    return this;
  }

  def appendElse(constraint: Constraint): ConstraintBuilder = {
    this.constraint.Else = constraint;
    return this;
  }

  def appendThen(constraint: Constraint): ConstraintBuilder = {
    this.constraint.Then = constraint;
    return this;
  }

  def Build: Constraint = return this.constraint;
}