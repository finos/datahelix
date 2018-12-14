package com.scottlogic.deg.mappers

trait Mapper[A,B] {
  def Map(original : A) : B
}
