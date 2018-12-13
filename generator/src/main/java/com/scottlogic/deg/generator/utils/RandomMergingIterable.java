package com.scottlogic.deg.generator.utils;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class RandomMergingIterable<E> implements Iterable<E> {
    private final List<Iterable<E>> iterableList;
    private final RandomNumberGenerator randomNumberGenerator;

    public RandomMergingIterable(List<Iterable<E>> iterableList, RandomNumberGenerator randomNumberGenerator) {
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
        private final RandomNumberGenerator randomNumberGenerator;

        private InternalIterator(List<Iterator<E>> iteratorList, RandomNumberGenerator randomNumberGenerator) {
            this.iteratorList = iteratorList;
            this.randomNumberGenerator = randomNumberGenerator;
        }

        @Override
        public boolean hasNext() {
            return iteratorList.stream().anyMatch(Iterator::hasNext);
        }

        @Override
        public E next() {
            List<Iterator<E>> nonEmptyIteratorsList = iteratorList
                .stream()
                .filter(iterator -> iterator.hasNext())
                .collect(Collectors.toList());

            return nonEmptyIteratorsList
                .get(randomNumberGenerator.nextInt(nonEmptyIteratorsList.size()))
                .next();
        }
    }
}
