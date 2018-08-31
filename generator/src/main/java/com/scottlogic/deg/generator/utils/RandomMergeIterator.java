package com.scottlogic.deg.generator.utils;

import java.util.Iterator;
import java.util.List;

// TODO Weight the probability of selecting each country by its value count?
public class RandomMergeIterator<E> implements Iterator<E> {

  private List<Iterator<E>> iteratorList;
  private IRandomNumberGenerator randomNumberGenerator;

  public RandomMergeIterator(List<Iterator<E>> iteratorList, IRandomNumberGenerator randomNumberGenerator) {
    this.iteratorList = iteratorList;
    this.randomNumberGenerator = randomNumberGenerator;
  }

  @Override
  public boolean hasNext() {
    return iteratorList.stream().anyMatch(Iterator::hasNext);
  }

  @Override
  public E next() {
    return iteratorList.get(randomNumberGenerator.nextInt(iteratorList.size())).next();
  }
}
