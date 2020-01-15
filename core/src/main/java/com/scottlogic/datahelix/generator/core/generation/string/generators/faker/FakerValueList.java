package com.scottlogic.datahelix.generator.core.generation.string.generators.faker;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FakerValueList implements List<String> {
    private List<String> underlyingList;
    private final Map<Integer, String> randomlyAccessedValues = new HashMap<>();

    public FakerValueList(List<String> underlyingList) {
        this.underlyingList = underlyingList;
    }

    public Map<Integer, String> getRandomlyAccessedValues() {
        return randomlyAccessedValues;
    }

    @Override
    public int size() {
        return underlyingList.size();
    }

    @Override
    public boolean isEmpty() {
        return underlyingList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return underlyingList.contains(o);
    }

    @Override
    public boolean add(String s) {
        return underlyingList.add(s);
    }

    @Override
    public boolean remove(Object o) {
        return underlyingList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return underlyingList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends String> c) {
        return underlyingList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        return underlyingList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return underlyingList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return underlyingList.retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<String> operator) {
        underlyingList.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super String> c) {
        underlyingList.sort(c);
    }

    @Override
    public void clear() {
        randomlyAccessedValues.clear();
        underlyingList.clear();
    }

    @Override
    public boolean equals(Object o) {
        return underlyingList.equals(o);
    }

    @Override
    public int hashCode() {
        return underlyingList.hashCode();
    }

    @Override
    public String get(int index) {
        String value = underlyingList.get(index);
        randomlyAccessedValues.put(index, value);
        return value;
    }

    @Override
    public String set(int index, String element) {
        if (randomlyAccessedValues.containsKey(index)) {
            randomlyAccessedValues.put(index, element);
        }

        return underlyingList.set(index, element);
    }

    @Override
    public void add(int index, String element) {
        underlyingList.add(index, element);
    }

    @Override
    public String remove(int index) {
        String value = underlyingList.remove(index);
        randomlyAccessedValues.remove(index, value);

        return value;
    }

    @Override
    public int indexOf(Object o) {
        return underlyingList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return underlyingList.lastIndexOf(o);
    }

    @Override
    public Iterator<String> iterator() {
        throw new UnsupportedOperationException("Would remove means to record which values are accessed");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Would remove means to record which values are accessed");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Would remove means to record which values are accessed");
    }

    @Override
    public ListIterator<String> listIterator() {
        throw new UnsupportedOperationException("Would remove means to record which values are accessed");
    }

    @Override
    public ListIterator<String> listIterator(int index) {
        throw new UnsupportedOperationException("Would remove means to record which values are accessed");
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Would remove means to record which values are accessed");
    }

    @Override
    public Spliterator<String> spliterator() {
        throw new UnsupportedOperationException("Would remove means to record which values are accessed");
    }

    public void removeAllValuesExceptLast() {
        if (underlyingList.size() <= 1) {
            randomlyAccessedValues.clear();
            return;
        }

        List<String> newList = IntStream.range(0, underlyingList.size())
            .filter(index -> !randomlyAccessedValues.containsKey(index))
            .mapToObj(index -> underlyingList.get(index))
            .collect(Collectors.toList());

        randomlyAccessedValues.clear();
        this.underlyingList = newList;
    }
}
