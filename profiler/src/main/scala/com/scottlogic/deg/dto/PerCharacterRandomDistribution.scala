package com.scottlogic.deg.dto

case class PerCharacterRandomDistribution (
  lengthMin: Int,
  lengthMax: Int,
  lengthAvg: Double,
  alphabet: String
) extends AbstractDistribution
