package com.scottlogic.deg.custom.builder;

import com.scottlogic.deg.custom.CustomGenerator;
import com.scottlogic.deg.custom.CustomGeneratorFieldType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CustomGeneratorBuilder<T> {

    private CustomGeneratorFieldType fieldType;
    private String name;

    private Function<T, Boolean> matchingFunction;

    private Supplier<Stream<T>> randomGenerator;
    private Supplier<Stream<T>> negatedRandomGenerator;
    private Supplier<Stream<T>> sequentialGenerator;
    private Supplier<Stream<T>> negatedSequentialGenerator;

    private CustomGeneratorBuilder(CustomGeneratorFieldType fieldType, String name) {
        this.fieldType = fieldType;
        this.name = name;
        matchingFunction = (value) -> { throw new CustomGeneratorNotImplementedException(name + " custom generator does not support being used with inSet or equalsTo constraints"); };
        randomGenerator = () -> { throw new CustomGeneratorNotImplementedException(name + " custom generator does not support random mode"); };
        negatedRandomGenerator = () -> { throw new CustomGeneratorNotImplementedException(name + " custom generator does not support being negated in random mode"); };
        sequentialGenerator = () -> { throw new CustomGeneratorNotImplementedException(name + " custom generator does not support sequential mode"); };
        negatedSequentialGenerator = () -> { throw new CustomGeneratorNotImplementedException(name + " custom generator does not support being negated in sequential mode"); };
    }

    public static CustomGeneratorBuilder<String> createStringGenerator(String name){
        return new CustomGeneratorBuilder<String>(CustomGeneratorFieldType.STRING, name);
    }

    public static CustomGeneratorBuilder<BigDecimal> createNumericGenerator(String name){
        return new CustomGeneratorBuilder<BigDecimal>(CustomGeneratorFieldType.NUMERIC, name);
    }

    public static CustomGeneratorBuilder<OffsetDateTime> createDateTimeGenerator(String name){
        return new CustomGeneratorBuilder<OffsetDateTime>(CustomGeneratorFieldType.DATETIME, name);
    }

    /**
     * the part of the generator to be used during random generation
     *
     * Required if you want your custom generator to support random generation
     * @param supplier Supplier for a random value
     * @return
     */
    public CustomGeneratorBuilder<T> withRandomGenerator(Supplier<T> supplier){
        this.randomGenerator = () -> Stream.generate(supplier);
        return this;
    }

    /**
     * the part of the generator to be used when the generator constraint is negated during random generation
     *  - this should be implemented as the values that your regular generator should not be outputting
     *
     * Required if you want your custom generator to support being negated in random mode
     * Required if you want your custom generator to support used in the IF part of IF THEN constraints
     * @param supplier Supplier for a random value that would not be produced by the non negated generator
     * @return
     */
    public CustomGeneratorBuilder<T> withNegatedRandomGenerator(Supplier<T> supplier){
        this.negatedRandomGenerator = () -> Stream.generate(supplier);
        return this;
    }

    /**
     * the part of the generator to be used during sequential generation
     *
     * Required if you want your custom generator to support sequential generation
     * Required if you want your custom generator to support unique keys generation
     * @param sequentialGenerator the supplier for a stream of sequential values
     * @return
     */
    public CustomGeneratorBuilder<T> withSequentialGenerator(Supplier<Stream<T>> sequentialGenerator) {
        this.sequentialGenerator = sequentialGenerator;
        return this;
    }

    /**
     * the part of the generator to be used when the generator constraint is negated during sequential generation
     *
     * Required if you want your custom generator to support being negated in sequential mode
     * should not be used with unique keys generation
     *
     * @param negatedSequentialGenerator the supplier for a stream of sequential values that would not be produced by the non negated generator
     * @return
     */
    public CustomGeneratorBuilder<T> withNegatedSequentialGenerator(Supplier<Stream<T>> negatedSequentialGenerator) {
        this.negatedSequentialGenerator = negatedSequentialGenerator;
        return this;
    }

    /**
     * The function to check whether a value from a set can be generated from this generator
     *
     * Required if you want your custom generator to support being combined wit
     * @param matchingFunction a function that takes a value and says whether it can be produced by the generator
     * @return
     */
    public CustomGeneratorBuilder<T> withMatchingFunction(Function<T, Boolean> matchingFunction) {
        this.matchingFunction = matchingFunction;
        return this;
    }

    public CustomGenerator<T> build(){
        if (name == null){
            throw new UnsupportedOperationException("Custom Generators must be named");
        }

        return new BuiltCustomGenerator<>(fieldType, name, matchingFunction, randomGenerator, negatedRandomGenerator, sequentialGenerator, negatedSequentialGenerator);
    }
}
