package com.scottlogic.deg.generator;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.function.Function;

public class LambdaMatcher<T> extends BaseMatcher<T> {
    public static <U> Matcher<U> isSuchThat(Function<U, Boolean> predicate) {
        return new LambdaMatcher<>(predicate, null);
    }

    public static <U> Matcher<U> isSuchThat(Function<U, Boolean> predicate, String description) {
        return new LambdaMatcher<>(predicate, description);
    }

    private final Function<T, Boolean> predicate;
    private final String description;

    private LambdaMatcher(Function<T, Boolean> predicate, String description) {
        this.predicate = predicate;
        this.description = description != null ? description : "Lambda did not match";
    }

    @Override
    public boolean matches(Object argument) {
        return predicate.apply((T) argument);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(this.description);
    }
}

