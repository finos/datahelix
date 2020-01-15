package com.scottlogic.datahelix.generator.core.generation.string.generators.faker;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FakerSpecFakeValueProvider implements FakeValueProvider {
    private final Locale locale;
    private final String fakerSpec;
    private List<String> allUnfilteredValues;

    public FakerSpecFakeValueProvider(Locale locale, String fakerSpec) {
        this.locale = locale;
        this.fakerSpec = fakerSpec;
    }

    @Override
    public boolean hasValue(String value, Function<String, Boolean> predicate) {
        return getAllValues(predicate).anyMatch(v -> v.equals(value));
    }

    @Override
    public Stream<String> getAllValues(Function<String, Boolean> predicate) {
        if (allUnfilteredValues == null) {
            allUnfilteredValues = getAllUnfilteredValues();
        }

        return allUnfilteredValues.stream().filter(predicate::apply);
    }

    private List<String> getAllUnfilteredValues() {
        FakerValueListExposingService fakerValueListExposingService = new FakerValueListExposingService(locale);
        Faker faker = new Faker(fakerValueListExposingService);

        String fakerExpression = "#{" + this.fakerSpec + "}";
        String singleFakerValue = faker.expression(fakerExpression);
        if (singleFakerValue == null) {
            throw new RuntimeException("Faker could not process the given specification: '" + this.fakerSpec + "'");
        }

        if (fakerValueListExposingService.fakeValues.size() == 1) {
            //only one field, all values are/should-be unique

            return fakerValueListExposingService.fakeValues.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        }

        /* more intensive strategy, where more than one field is used: minimal strategy
         every time a value for each 'list' is returned, remove it from Faker */

        List<String> fakerValues = new ArrayList<>();
        fakerValues.add(singleFakerValue);

        while (true) {
            fakerValueListExposingService.removeUsedFakerValuesExceptLast();

            if (!fakerValueListExposingService.anyFakeValueListHasMoreThanOneValueLeft()) {
                break;
            }

            String thisFakerValue = faker.expression(fakerExpression);
            fakerValues.add(thisFakerValue);
        }

        return fakerValues;
    }

    private static class FakerValueListExposingService extends FakeValuesService {
        public Map<String, FakerValueList> fakeValues = new HashMap<>();

        public FakerValueListExposingService(Locale locale) {
            super(locale, new RandomService(null));
        }

        public boolean anyFakeValueListHasMoreThanOneValueLeft(){
            return fakeValues.values().stream()
                .anyMatch(values -> values.size() > 1);
        }

        public void removeUsedFakerValuesExceptLast() {
            fakeValues.values().forEach(FakerValueList::removeAllValuesExceptLast);
        }

        @Override
        public Object fetch(String key) {
            Object result = super.fetch(key);
            if (result != null) {
                fakeValues.put(key, extractValues(result));
            }

            return result;
        }

        @Override
        public Object fetchObject(String key) {
            Object result = super.fetchObject(key);
            if (result != null) {
                fakeValues.put(key, extractValues(result));
            }

            return result;
        }

        private FakerValueList extractValues(Object fakeValues) {
            if (fakeValues == null){
                return null;
            }

            if (fakeValues instanceof List) {
                //noinspection unchecked
                List<String> valuesList = ((List<String>) fakeValues);
                return new FakerValueList(valuesList);
            }

            throw new RuntimeException("Unsure how to extract fake values from a " + fakeValues.getClass().getName());
        }
    }
}
