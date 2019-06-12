package com.scottlogic.deg.profile.reader.file.inputstream;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Function;

public class ClasspathMapper implements Function<String, InputStream> {

    @Override
    public InputStream apply(String path) {
        return Optional.ofNullable(this.getClass()
            .getClassLoader()
            .getResourceAsStream(path)
        ).orElseThrow(() -> new IllegalArgumentException("Classpath does not contain path " + path));
    }
}
