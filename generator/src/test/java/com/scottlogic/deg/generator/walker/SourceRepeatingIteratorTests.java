package com.scottlogic.deg.generator.walker;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

class SourceRepeatingIteratorTests {
    @Test
    public void hasNextShouldReturnFalseIfSourceProviderReturnsEmptyIterator(){
        SourceRepeatingIterator<String> repeatingIterator = new SourceRepeatingIterator<>(
            1,
            Collections::emptyIterator
        );

        boolean hasNext = repeatingIterator.hasNext();

        Assert.assertThat(hasNext, is(false));
    }

    @Test
    public void hasNextShouldReturnTrueIfSourceProviderReturnsNonEmptyIterator(){
        SourceRepeatingIterator<String> repeatingIterator = new SourceRepeatingIterator<>(
            1,
            () -> Collections.singletonList("abc").iterator()
        );

        boolean hasNext = repeatingIterator.hasNext();

        Assert.assertThat(hasNext, is(true));
    }

    @Test
    public void nextShouldAskForNextItemFromSourceIterator(){
        Iterator<String> underlyingIterator = mock(Iterator.class);
        SourceRepeatingIterator<String> repeatingIterator = new SourceRepeatingIterator<>(
            1,
            () -> underlyingIterator
        );
        when(underlyingIterator.hasNext()).thenReturn(true);

        if (repeatingIterator.hasNext()) {
            repeatingIterator.next();
        }

        verify(underlyingIterator).next();
    }

    @Test
    public void shouldGetANewSourceOnceAtEndOfPermittedRangeOfItemsFromSource(){
        AtomicInteger noOfSourcesProvided = new AtomicInteger();
        SourceRepeatingIterator<Integer> repeatingIterator = new SourceRepeatingIterator<>(
            1,
            () -> Arrays.asList(0, noOfSourcesProvided.incrementAndGet()).iterator()
        );
        if (repeatingIterator.hasNext()) {
            repeatingIterator.next();
        }

        if (repeatingIterator.hasNext()) {
            repeatingIterator.next();
        }

        Assert.assertThat(noOfSourcesProvided.get(), equalTo(2));
    }

    @Test
    public void shouldContinueUntilEndOfSourceIfNoLimitPlacedOnItemsFromSource(){
        AtomicInteger noOfSourcesProvided = new AtomicInteger();
        SourceRepeatingIterator<Integer> repeatingIterator = new SourceRepeatingIterator<>(
            null,
            () -> Arrays.asList(0, noOfSourcesProvided.incrementAndGet()).iterator()
        );
        if (repeatingIterator.hasNext()) {
            repeatingIterator.next();
        }
        if (repeatingIterator.hasNext()) {
            repeatingIterator.next();
        }

        if (repeatingIterator.hasNext()) {
            repeatingIterator.next();
        }

        Assert.assertThat(noOfSourcesProvided.get(), equalTo(2));
    }

    @Test
    public void shouldAbortIfNewSourceOfItemsIsNull(){
        Queue<Iterator<String>> queueOfIterators = new LinkedList<>(Arrays.asList(
            Arrays.asList("a", "b").iterator(),
            null
        ));
        SourceRepeatingIterator<String> repeatingIterator = new SourceRepeatingIterator<>(
            null,
            queueOfIterators::poll //retrieves the head of the queue and removes it from <queueOfIterators> a.k.a. pop()
        );

        List<String> values = new ArrayList<>();
        repeatingIterator.forEachRemaining(values::add);

        Assert.assertThat(values, hasItems("a", "b"));
    }
}