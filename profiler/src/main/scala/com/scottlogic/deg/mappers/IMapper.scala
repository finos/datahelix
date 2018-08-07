package com.scottlogic.deg.mappers

trait IMapper[A,B] {
  def Map(original : A) : B
}
