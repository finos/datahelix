package com.scottlogic.deg.profile.reader.inputstream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.function.Function;

public class PathMapper implements Function<String, InputStream> {

    @Override
    public InputStream apply(String s) {
        try {
            return new FileInputStream(s);
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
