package com.scottlogic.deg.dto

case class NumericField(
  name:String,
  meanAvg:Number,
  stdDev: Number,
  min:Number,
  max:Number,
  nullPrevalence:Number
) extends AbstractField