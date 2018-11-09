package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.ProfileFields;

import java.util.Collection;
import java.util.stream.Collectors;

public class ProfileFieldComparer implements IEqualityComparer {
    private final IEqualityComparer collectionComparer = new AnyOrderCollectionEqualityComparer();
    private final TreeComparisonContext context;

    public ProfileFieldComparer(TreeComparisonContext context) {
        this.context = context;
    }

    @Override
    public int getHashCode(Object item) {
        return item.hashCode();
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((ProfileFields)item1, (ProfileFields)item2);
    }

    private boolean equals(ProfileFields firstProfileFields, ProfileFields secondProfileFields) {
        Collection firstProfileFieldsCollection = firstProfileFields.stream().collect(Collectors.toList());
        Collection secondProfileFieldsCollection = secondProfileFields.stream().collect(Collectors.toList());

        boolean equals = collectionComparer.equals(firstProfileFieldsCollection, secondProfileFieldsCollection);
        if (!equals) {
            context.reportDifferences(
                firstProfileFieldsCollection,
                secondProfileFieldsCollection,
                TreeComparisonContext.TreeElementType.Fields
            );
        }

        return equals;
    }
}
