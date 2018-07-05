package com.scottlogic.deg.dto

case class EnumField(
  name:String,
  nullPrevalence:Number,
  distribution: AbstractDistribution
) extends AbstractField