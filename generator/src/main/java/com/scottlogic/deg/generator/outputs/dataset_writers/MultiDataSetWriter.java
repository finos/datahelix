package com.scottlogic.deg.generator.outputs.dataset_writers;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MultiDataSetWriter implements DataSetWriter<MultiDataSetWriter.MultiCloseable> {
    private final List<DataSetWriter> writers;

    public MultiDataSetWriter(DataSetWriter... writers) {
        this(Arrays.asList(writers));
    }

    public MultiDataSetWriter(List<DataSetWriter> writers) {
        this.writers = writers;
    }

    @Override
    public MultiCloseable openWriter(Path directory, String filenameWithoutExtension, ProfileFields profileFields) throws IOException {
        ThrownExceptions exceptions = new ThrownExceptions();

        return exceptions.rethrowIfCaughtOrReturn(new MultiCloseable(
            this.writers
                .stream()
                .collect(Collectors.toMap(
                    Function.identity(),
                    writer -> {
                        try {
                            return writer.openWriter(directory, filenameWithoutExtension, profileFields);
                        } catch (IOException e) {
                            exceptions.add(e);
                            return new NullClosable();
                        }
                    }
                ))
        ));
    }

    @Override
    public void writeRow(MultiCloseable closeable, GeneratedObject row) throws IOException {
        ThrownExceptions exceptions = new ThrownExceptions();

        closeable.writers.forEach((writer, writerClosable) -> {
            try {
                writer.writeRow(writerClosable, row);
            } catch (IOException e) {
                exceptions.add(e);
            }
        });

        exceptions.rethrowIfCaught();
    }

    class ThrownExceptions{
        final ArrayList<IOException> exceptions = new ArrayList<>();

        void add(IOException exception){
            exceptions.add(exception);
        }

        <T> T rethrowIfCaughtOrReturn(T ifNoExceptions) throws IOException {
            rethrowIfCaught();
            return ifNoExceptions;
        }

        void rethrowIfCaught() throws IOException {
            if (exceptions.isEmpty()){
                return;
            }

            throw this.exceptions.get(0); //TODO: include detail from all other exceptions, e.g. an aggregatge exception
        }
    }

    class NullClosable implements Closeable{
        @Override
        public void close() { }
    }

    class MultiCloseable implements Closeable {
        final Map<DataSetWriter, Closeable> writers;

        MultiCloseable(Map<DataSetWriter, Closeable> writers) {
            this.writers = writers;
        }

        @Override
        public void close() throws IOException {
            ThrownExceptions exceptions = new ThrownExceptions();

            for (Closeable closable : this.writers.values()) {
                try {
                    closable.close();
                } catch (IOException e) {
                    exceptions.add(e);
                }
            }

            exceptions.rethrowIfCaught();
        }
    }
}
