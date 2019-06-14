package com.scottlogic.deg.profile.reader.file;

import java.util.stream.Stream;

@FunctionalInterface
public interface PathToStringsLoader {

    Stream<String> retrieveNames(String path);

}
