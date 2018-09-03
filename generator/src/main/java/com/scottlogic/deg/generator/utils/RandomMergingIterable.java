package com.scottlogic.deg.generator.utils;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

// TODO Weight the probability of selecting each iterable by its value count?
public class RandomMergingIterable<E> implements Iterable<E> {
  private final List<Iterable<E>> iterableList;
  private final IRandomNumberGenerator randomNumberGenerator;

  public RandomMergingIterable(List<Iterable<E>> iterableList, IRandomNumberGenerator randomNumberGenerator) {
    this.iterableList = iterableList;
    this.randomNumberGenerator = randomNumberGenerator;
  }

  @Override
  public Iterator<E> iterator() {
    final List<Iterator<E>> iteratorList = iterableList.stream()
      .map(Iterable::iterator)
      .collect(Collectors.toList());
    return new InternalIterator(iteratorList, randomNumberGenerator);
  }

  private class InternalIterator implements Iterator<E> {

    private final List<Iterator<E>> iteratorList;
    private final IRandomNumberGenerator randomNumberGenerator;

    private InternalIterator(List<Iterator<E>> iteratorList, IRandomNumberGenerator randomNumberGenerator) {
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

}
