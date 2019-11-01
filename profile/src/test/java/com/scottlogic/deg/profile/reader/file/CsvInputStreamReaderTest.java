package com.scottlogic.deg.profile.reader.file;

import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.fieldspecs.whitelist.WeightedElement;
import com.scottlogic.deg.profile.reader.CsvInputStreamReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CsvInputStreamReaderTest {

    @Test
    public void testReadingLinesFromNames() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InputStream is = loader.getResourceAsStream("names/firstname.csv");

        final DistributedList<String> names = CsvInputStreamReader.retrieveLines(is);

        final Set<String> sampleNames = Stream.of("Rory", "Kyle", "Grace").collect(Collectors.toSet());

        assertTrue(names.list().containsAll(sampleNames));
    }

    @Test
    public void testReadingLinesFromFileWithoutFrequencies() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InputStream is = loader.getResourceAsStream("csv/without-frequencies.csv");

        final DistributedList<String> set = CsvInputStreamReader.retrieveLines(is);

        assertTrue(checkAllWeightsAreEquals(set));
    }

    private <T> boolean checkAllWeightsAreEquals(DistributedList<T> set) {
        return set.distributedList().stream()
            .map(WeightedElement::weight)
            .distinct()
            .limit(2).count() <= 1;
    }

}