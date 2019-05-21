package com.scottlogic.deg.output.writer;

import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.output.writer.DataSetWriter;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/** When asked to write data, passes it on to all of its sub-writers */
public class SplittingDataSetWriter implements DataSetWriter {
    private final Collection<DataSetWriter> subWriters;

    public SplittingDataSetWriter(Collection<DataSetWriter> subWriters) {
        this.subWriters = subWriters;
    }

    @Override
    public void writeRow(GeneratedObject row) throws IOException {
        doCatchAndThrow(writer -> writer.writeRow(row));
    }

    @Override
    public void close() throws IOException {
        doCatchAndThrow(Closeable::close);
    }

    private void doCatchAndThrow(WriterAction action) throws IOException {
        ThrownExceptions exceptions = new ThrownExceptions();

        for (DataSetWriter writer : subWriters) {
            try {
                action.accept(writer);
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

    interface WriterAction {
        void accept(DataSetWriter writer) throws IOException;
    }
}
