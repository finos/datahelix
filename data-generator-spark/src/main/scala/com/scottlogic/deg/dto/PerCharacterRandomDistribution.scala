package com.scottlogic.deg.dto

case class PerCharacterRandomDistribution (
  lengthMin: Int,
  lengthMax: Int,
  alphabet: String
) extends AbstractDistribution