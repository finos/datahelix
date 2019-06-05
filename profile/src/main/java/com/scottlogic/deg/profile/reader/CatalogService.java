package com.scottlogic.deg.profile.reader;

import java.util.Set;

public interface CatalogService<T, O> {

    Set<O> retrieveValues(T configuration);

}
