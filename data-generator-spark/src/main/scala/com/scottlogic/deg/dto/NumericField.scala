package com.scottlogic.deg.dto

case class NumericField(
  name:String,
  nullPrevalence:Number,
  distribution: AbstractDistribution,
  format: AbstractNumericFormat
) extends AbstractField