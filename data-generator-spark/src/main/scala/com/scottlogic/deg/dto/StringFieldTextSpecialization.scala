package com.scottlogic.deg.dto

case class StringFieldTextSpecialization(
  lenMin: Integer,
  lenMax: Integer,
  lenMeanAvg: Number,
  lenStdDev: Number
) extends StringFieldAbstractSpecialization