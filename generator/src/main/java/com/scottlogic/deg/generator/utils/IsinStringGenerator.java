package com.scottlogic.deg.generator.utils;

public class IsinStringGenerator implements IStringGenerator {

  @Override
  public IStringGenerator intersect(IStringGenerator stringGenerator) {
    return null;
  }

  @Override
  public IStringGenerator complement() {
    return null;
  }

  @Override
  public boolean isFinite() {
    return false;
  }

  @Override
  public long getValueCount() {
    return 0;
  }

  @Override
  public Iterable<String> generateAllValues() {
    return null;
  }

  @Override
  public Iterable<String> generateRandomValues(IRandomNumberGenerator randomNumberGenerator) {
    return null;
  }
}
