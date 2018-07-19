package com.scottlogic.deg.dto

case class TextField(
  name:String,
  nullPrevalence:Number,
  distribution: AbstractDistribution
) extends AbstractField
