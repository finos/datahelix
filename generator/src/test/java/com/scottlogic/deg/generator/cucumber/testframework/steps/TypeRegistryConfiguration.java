package com.scottlogic.deg.generator.cucumber.testframework.steps;

import com.fasterxml.jackson.core.JsonParseException;
import com.scottlogic.deg.generator.cucumber.testframework.utils.CucumberGenerationMode;
import com.scottlogic.deg.generator.cucumber.testframework.utils.GeneratorTestUtilities;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.schemas.v0_1.AtomicConstraintType;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;
import io.cucumber.datatable.TableCellByTypeTransformer;

import java.util.*;
import java.util.stream.Collectors;

public class TypeRegistryConfiguration implements TypeRegistryConfigurer {

    private final Set<AtomicConstraintType> allOperators = new HashSet<>(Arrays.asList(AtomicConstraintType.values()));

    @Override
    public Locale locale(){
        return Locale.ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry tr) {
        this.defineDataGenerationStrategyType(tr);
        this.defineCombinationStrategyType(tr);
        this.defineWalkerType(tr);
        this.defineOperationParameterType(tr);
        this.defineGenerationMode(tr);
        this.defineParameterType(tr,"fieldVar", "^(.+)");
        this.defineParameterType(tr,"regex", "/(.+)/$");
        tr.setDefaultDataTableCellTransformer(new DataTableCellTransformer());

        tr.defineParameterType(new ParameterType<>(
            "number",
            "([+-]?\\d+(\\.\\d+)?)",
            Number.class,
            (Transformer<Number>) value -> (Number) GeneratorTestUtilities.parseNumber(value)));

        tr.defineParameterType(new ParameterType<>(
            "boolean",
            "(true|false)$",
            Boolean.class,
            (Transformer<Boolean>) Boolean::valueOf));

        tr.defineParameterType(new ParameterType<>(
            "date",
            DateValueStep.DATE_REGEX,
            DateObject.class,
            DateObject::new));
    }

    private void defineOperationParameterType(TypeRegistry tr){
        tr.defineParameterType(new ParameterType<>(
            "operator",
            this.getHumanReadableOperationRegex(allOperators),
            String.class,
            this::extractConstraint
        ));
    }

    private void defineParameterType(TypeRegistry tr, String name, String regex) {
        tr.defineParameterType(new ParameterType<>(
            name,
            regex,
            String.class,
            (Transformer<String>)fieldName -> fieldName));
    }

    private void defineDataGenerationStrategyType(TypeRegistry tr){
        Transformer<GenerationConfig.DataGenerationType> transformer = strategyString ->
            Arrays.stream(GenerationConfig.DataGenerationType.values())
            .filter(val -> val.toString().equalsIgnoreCase(strategyString))
            .findFirst().orElse(GenerationConfig.DataGenerationType.FULL_SEQUENTIAL);

        tr.defineParameterType(new ParameterType<>(
            "generationStrategy",
            "(.*)$",
            GenerationConfig.DataGenerationType.class,
            transformer));
    }

    private void defineCombinationStrategyType(TypeRegistry tr){
        Transformer<GenerationConfig.CombinationStrategyType> transformer = strategyString ->
            Arrays.stream(GenerationConfig.CombinationStrategyType.values())
                .filter(val -> val.toString().equalsIgnoreCase(strategyString))
                .findFirst().orElse(GenerationConfig.CombinationStrategyType.PINNING);

        tr.defineParameterType(new ParameterType<>(
            "combinationStrategy",
            "(.*)$",
            GenerationConfig.CombinationStrategyType.class,
            transformer));
    }

    private void defineWalkerType(TypeRegistry tr) {
        Transformer<GenerationConfig.TreeWalkerType> transformer = strategyString ->
            Arrays.stream(GenerationConfig.TreeWalkerType.values())
                .filter(val -> val.toString().equalsIgnoreCase(strategyString))
                .findFirst().orElse(GenerationConfig.TreeWalkerType.CARTESIAN_PRODUCT);

        tr.defineParameterType(new ParameterType<>(
            "walkerType",
            "(.*)$",
            GenerationConfig.TreeWalkerType.class,
            transformer));
    }

    private void defineGenerationMode(TypeRegistry tr) {
        Transformer<CucumberGenerationMode> transformer = strategyString ->
            Arrays.stream(CucumberGenerationMode.values())
                .filter(val -> val.toString().equalsIgnoreCase(strategyString))
                .findFirst().orElse(CucumberGenerationMode.VALIDATING);

        tr.defineParameterType(new ParameterType<>(
            "generationMode",
            "(.*)$",
            CucumberGenerationMode.class,
            transformer));
    }

    private String extractConstraint(String gherkinConstraint) {
        List<String> allConstraints = Arrays.asList(gherkinConstraint.split(" "));
        return allConstraints.get(0) + allConstraints
            .stream()
            .skip(1)
            .map(value -> value.substring(0, 1).toUpperCase() + value.substring(1))
            .collect(Collectors.joining());
    }

    private String getHumanReadableOperationRegex(Set<AtomicConstraintType> types){
        return
            types.stream()
            .map(act -> act.toString().replaceAll("([a-z])([A-Z]+)", "$1 $2").toLowerCase())
            .collect(Collectors.joining("|", "(", ")"));
    }

    private class DataTableCellTransformer implements TableCellByTypeTransformer {
        @Override
        public <T> T transform(String value, Class<T> aClass) throws Throwable {
            try {
                return aClass.cast(GeneratorTestUtilities.parseInput(value.trim()));
            } catch (JsonParseException | InvalidProfileException e) {
                return (T)e;
            }
        }
    }
}
