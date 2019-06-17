package com.scottlogic.deg.profile.reader.file;

import java.io.InputStream;
import java.util.Set;

public interface PathToStringsLoader {

    Set<String> retrieveNames(InputStream stream);

}
