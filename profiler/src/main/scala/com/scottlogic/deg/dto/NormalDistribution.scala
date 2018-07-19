package com.scottlogic.deg.dto

case class NormalDistribution (
  meanAvg: Number,
  stdDev: Number,
  min: Number,
  max: Number
) extends AbstractDistribution