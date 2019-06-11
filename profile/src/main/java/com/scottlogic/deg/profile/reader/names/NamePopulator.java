package com.scottlogic.deg.profile.reader.names;

import java.util.Set;

public interface NamePopulator<T> {

    Set<NameHolder> retrieveNames(T config);
}
