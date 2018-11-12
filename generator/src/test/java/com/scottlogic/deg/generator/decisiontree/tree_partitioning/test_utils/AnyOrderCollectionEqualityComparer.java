package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import java.util.ArrayList;
import java.util.Collection;

public class AnyOrderCollectionEqualityComparer implements EqualityComparer, CollectionEqualityComparer {
    private final EqualityComparer itemEqualityComparer;

    public AnyOrderCollectionEqualityComparer() {
        this.itemEqualityComparer = new DefaultEqualityComparer(this);
    }

    public AnyOrderCollectionEqualityComparer(EqualityComparer itemEqualityComparer) {
        this.itemEqualityComparer = itemEqualityComparer;
    }

    @Override
    public int getHashCode(Object item) {
        Collection collection = (Collection)item;
        return collection.size();
    }

    @Override
    public boolean equals(Object x, Object y) {
        Collection a = (Collection) x;
        Collection b = (Collection) y;

        if (!collectionsContainMatchingItems(a, b)) {
            return false;
        }

        return collectionsContainMatchingItems(b, a);
    }

    public ArrayList<Object> getItemsMissingFrom(Collection x, Collection y) {
        ArrayList<Object> itemsMissing = new ArrayList<>();

        for (Object itemFromX : x) {
            Object itemFromY = findItem(itemFromX, y);

            if (itemFromY == null) {
                itemsMissing.add(itemFromX);
            }
        }

        return itemsMissing;
    }

    private boolean collectionsContainMatchingItems(Collection a, Collection b) {
        for (Object itemFromA : a) {
            Object itemFromB = findItem(itemFromA, b);

            if (itemFromB == null) {
                return false;
            }
        }

        return true;
    }

    private Object findItem(Object toFind, Collection collection){
        for (Object item : collection) {
            if (this.itemEqualityComparer.equals(toFind, item))
                return item;
        }

        return null;
    }
}
