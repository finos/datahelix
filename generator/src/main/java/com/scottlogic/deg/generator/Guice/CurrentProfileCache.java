package com.scottlogic.deg.generator.Guice;

import com.scottlogic.deg.generator.Profile;

/**
 * Defines a store of the current profile under generation. Is only consumed in one place:
 * HierarchicalDependencyFixFieldStrategy.
 * */
public class CurrentProfileCache {
    public Profile profile;
}
