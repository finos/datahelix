package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import java.util.ArrayList;
import java.util.Collection;

public class AnyOrderCollectionEqualityComparer implements IEqualityComparer {
    private final IEqualityComparer itemEqualityComparer;
    public ArrayList itemsMissingFromCollection1;
    public ArrayList itemsMissingFromCollection2;
    public boolean reportErrors = false;

    public AnyOrderCollectionEqualityComparer() {
        this.itemEqualityComparer = new DefaultEqualityComparer(this);
    }

    public AnyOrderCollectionEqualityComparer(IEqualityComparer itemEqualityComparer) {
        this.itemEqualityComparer = itemEqualityComparer;
    }

    @Override
    public int getHashCode(Object item) {
        Collection collection = (Collection)item;
        return collection.size();
    }

    @Override
    public boolean equals(Object x, Object y) {
        Collection collection1 = (Collection) x;
        Collection collection2 = (Collection) y;
        boolean pass = true;
        itemsMissingFromCollection1 = new ArrayList();
        itemsMissingFromCollection2 = new ArrayList();

        for (Object item1 : collection1){
            Object item2 = findItem(item1, collection2);

            if (item2 == null){
                pass = false;

                if (!reportErrors) {
                    return false; //cannot find <item1> in <collection2>
                }

                itemsMissingFromCollection2.add(item1);
            }
        }

        if (collection1.size() == collection2.size() && !reportErrors) {
            //if collections are the same size then items in <collection2> must relate to items in <collection1>
            return pass;
        }

        for (Object item2 : collection2){
            Object item1 = findItem(item2, collection1);

            if (item1 == null){
                pass = false;

                if (!reportErrors) {
                    return false; //cannot find <item2> in <collection1>
                }

                itemsMissingFromCollection1.add(item2);
            }
        }

        return pass;
    }

    private Object findItem(Object toFind, Collection collection){
        for (Object item : collection) {
            if (this.itemEqualityComparer.equals(toFind, item))
                return item;
        }

        return null;
    }
}
