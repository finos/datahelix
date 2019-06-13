package com.scottlogic.deg.profile.reader.file;

import com.scottlogic.deg.profile.reader.file.parser.StringCSVPopulator;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Set;
import java.util.function.Function;

public class CSVPathSetMapper implements Function<String, Set<String>> {

    private final Function<String, InputStream> pathStreamMapper;

    private final Function<InputStream, Set<String>> streamSetMapper;

    public CSVPathSetMapper(final Function<String, InputStream> pathStreamMapper) {
        this.pathStreamMapper = pathStreamMapper;
        streamSetMapper = new StringCSVPopulator();
    }

    @Override
    public Set<String> apply(String path) {
        InputStream stream = pathStreamMapper.apply(path);
        Set<String> result = streamSetMapper.apply(stream);
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
