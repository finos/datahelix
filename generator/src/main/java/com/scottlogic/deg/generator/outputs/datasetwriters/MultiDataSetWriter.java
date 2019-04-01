package com.scottlogic.deg.generator.outputs.datasetwriters;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class MultiDataSetWriter implements DataSetWriter<Closeable> {
    private final List<StatefulDataSetWriter> writers;

    public MultiDataSetWriter(DataSetWriter... writers) {
        this.writers = Arrays.stream(writers).map(StatefulDataSetWriter::new).collect(Collectors.toList());
    }

    @Override
    public Closeable openWriter(Path directory, String fileName, ProfileFields profileFields) throws IOException {
        ThrownExceptions exceptions = new ThrownExceptions();

        this.writers.forEach(writer -> {
            try {
                writer.openWriterAndRemember(directory, profileFields);
            } catch (IOException e) {
                exceptions.add(e);
            }
        });

        exceptions.rethrowIfCaught();
        return this::closeAllWriters; //to ensure when close() is called then its called on all instances in writers.
    }

    @Override
    public void writeRow(Closeable closeable, GeneratedObject row) throws IOException {
        ThrownExceptions exceptions = new ThrownExceptions();

        this.writers.forEach(writer -> {
            try {
                writer.writeRow(row);
            } catch (IOException e) {
                exceptions.add(e);
            }
        });

        exceptions.rethrowIfCaught();
    }

    @Override
    public String getFileName(String fileNameWithoutExtension) {
        String firstFileName = null;
        for (StatefulDataSetWriter writerWithFileName : this.writers){
            String fileName = writerWithFileName.getFileNameAndRemember(fileNameWithoutExtension);
            if (firstFileName == null && fileName != null){
                firstFileName = fileName;
            }
        }

        return firstFileName;
    }

    private void closeAllWriters() throws IOException {
        ThrownExceptions exceptions = new ThrownExceptions();

        for (StatefulDataSetWriter writer : this.writers) {
            if (writer.closeable == null){
                continue;
            }

            try {
                writer.closeable.close();
            } catch (IOException e) {
                exceptions.add(e);
            }
        }

        exceptions.rethrowIfCaught();
    }

    class ThrownExceptions{
        final ArrayList<IOException> exceptions = new ArrayList<>();

        void add(IOException exception){
            exceptions.add(exception);
        }

        void rethrowIfCaught() throws IOException {
            if (exceptions.isEmpty()){
                return;
            }

            throw this.exceptions.get(0); //TODO: include detail from all other exceptions, e.g. an aggregatge exception
        }
    }

    class StatefulDataSetWriter {
        private final DataSetWriter writer;
        private String fileName;
        private Closeable closeable;

        StatefulDataSetWriter(DataSetWriter writer) {
            this.writer = writer;
        }

        void openWriterAndRemember(Path directory, ProfileFields profileFields) throws IOException {
            if (this.fileName == null){
                throw new IllegalStateException("Filename has not been determined");
            }

            this.closeable = this.writer.openWriter(directory, this.fileName, profileFields);
        }

        void writeRow(GeneratedObject row) throws IOException {
            if (this.closeable == null){
                throw new IllegalStateException("Writer has not been initialised");
            }

            this.writer.writeRow(this.closeable, row);
        }

        String getFileNameAndRemember(String fileNameWithoutExtension) {
            this.fileName = this.writer.getFileName(fileNameWithoutExtension);
            return this.fileName;
        }
    }
}
