package com.scottlogic.deg.generator.decisiontree.testutils;

import com.scottlogic.deg.generator.ProfileFields;

import java.util.Collection;
import java.util.stream.Collectors;

public class ProfileFieldComparer implements EqualityComparer {
    private final EqualityComparer profileFieldsEqualityComparer;
    private final CollectionEqualityComparer profileFieldsCollectionEqualityComparer;
    private final TreeComparisonContext context;

    public ProfileFieldComparer(TreeComparisonContext context,
                                EqualityComparer profileFieldsEqualityComparer,
                                CollectionEqualityComparer profileFieldsCollectionEqualityComparer) {
        this.context = context;
        this.profileFieldsEqualityComparer = profileFieldsEqualityComparer;
        this.profileFieldsCollectionEqualityComparer = profileFieldsCollectionEqualityComparer;
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

        boolean equals = profileFieldsEqualityComparer.equals(firstProfileFieldsCollection, secondProfileFieldsCollection);
        if (!equals) {
            context.reportDifferences(
                profileFieldsCollectionEqualityComparer.getItemsMissingFrom(
                    firstProfileFieldsCollection,
                    secondProfileFieldsCollection
                ),
                profileFieldsCollectionEqualityComparer.getItemsMissingFrom(
                    secondProfileFieldsCollection,
                    firstProfileFieldsCollection
                ),
                TreeComparisonContext.TreeElementType.FIELDS
            );
        }

        return equals;
    }
}
