package com.scottlogic.deg.profile.reader.file;

import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.generator.fieldspecs.whitelist.WeightedElement;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class CsvInputStreamReaderTest {

    @Test
    public void testReadingLinesFromNames() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InputStream is = loader.getResourceAsStream("names/firstname.csv");

        final DistributedSet<String> names = CsvInputStreamReader.retrieveLines(is);

        // Weight extracted from name file by computing the total and dividing
        WeightedElement<String> sampleName = new WeightedElement<>("Rory", 0.0072439485F);

        assertTrue(names.distributedSet().contains(sampleName));
    }

    @Test
    public void testReadingLinesFromFileWithoutFrequencies() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InputStream is = loader.getResourceAsStream("csv/without-frequencies.csv");

        final DistributedSet<String> set = CsvInputStreamReader.retrieveLines(is);

        assertTrue(checkAllWeightsAreEquals(set));
    }

    private <T> boolean checkAllWeightsAreEquals(DistributedSet<T> set) {
        return set.distributedSet().stream()
            .map(WeightedElement::weight)
            .distinct()
            .limit(2).count() <= 1;
    }

}