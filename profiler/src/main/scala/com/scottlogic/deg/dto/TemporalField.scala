package com.scottlogic.deg.dto

case class TemporalField(
  name:String,
  nullPrevalence:Number,
  distribution: AbstractDistribution
) extends AbstractField