package com.scottlogic.deg.profile.reader.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Set;
import java.util.function.Function;

public class CSVSetMapper<T> implements Function<String, Set<T>> {

    private final Function<String, InputStream> pathStreamMapper;

    private final Function<InputStream, Set<T>> streamSetMapper;

    public CSVSetMapper(final Function<String, InputStream> pathStreamMapper,
                        final Function<InputStream, Set<T>> streamSetMapper) {
        this.pathStreamMapper = pathStreamMapper;
        this.streamSetMapper = streamSetMapper;
    }

    @Override
    public Set<T> apply(String path) {
        InputStream stream = pathStreamMapper.apply(path);
        Set<T> result = streamSetMapper.apply(stream);
        closeQuietly(stream);
        return result;
    }

    private static void closeQuietly(InputStream stream) {
        try {
            stream.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
