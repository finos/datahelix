package com.scottlogic.deg.generator.cucumber.utils;

import com.scottlogic.deg.schemas.v3.AtomicConstraintType;
import static com.scottlogic.deg.schemas.v3.AtomicConstraintType.*;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;

import java.util.*;
import java.util.stream.Collectors;

public class TypeRegistryConfiguration implements TypeRegistryConfigurer {

    private final Set<AtomicConstraintType> numericValueOperations = new HashSet<>(Arrays.asList(
        ISEQUALTOCONSTANT,
        HASLENGTH,
        ISSTRINGLONGERTHAN,
        ISSTRINGSHORTERTHAN,
        ISGREATERTHANCONSTANT,
        ISGREATERTHANOREQUALTOCONSTANT,
        ISLESSTHANCONSTANT,
        ISLESSTHANOREQUALTOCONSTANT,
        ISGRANULARTO
    ));

    private final Set<AtomicConstraintType> stringValueOperations = new HashSet<>(Arrays.asList(
        ISEQUALTOCONSTANT,
        AVALID,
        ISOFTYPE,
        FORMATTEDAS
    ));

    private final Set<AtomicConstraintType> regexValueOperations = new HashSet<>(Arrays.asList(
        MATCHESREGEX,
        CONTAINSREGEX
    ));

    private final Set<AtomicConstraintType> setValueOperations = new HashSet<>(Arrays.asList(
        ISINSET
    ));

    private final Set<AtomicConstraintType> dateValueOperations = new HashSet<>(Arrays.asList(
        ISEQUALTOCONSTANT,
        ISAFTERCONSTANTDATETIME,
        ISAFTEROREQUALTOCONSTANTDATETIME,
        ISBEFORECONSTANTDATETIME,
        ISBEFOREOREQUALTOCONSTANTDATETIME
    ));

    @Override
    public Locale locale(){
        return Locale.ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry tr) {
        this.defineParameterType(tr,"fieldVar", "^(.+)");
        this.defineParameterType(tr,"dateString", "((20\\d{2})-(\\d{2})-(\\d{2}T(\\d{2}:\\d{2}:\\d{2}\\.\\d{3})))$");
        this.defineParameterType(tr,"regex", "/(.+)/$");
        this.defineParameterType(tr,"set", "\\[(((.+))(, (.+))*)\\]");
        this.defineOperationParameterType(tr,"numericValueOperation", numericValueOperations);
        this.defineOperationParameterType(tr,"stringValueOperation", stringValueOperations);
        this.defineOperationParameterType(tr,"regexValueOperation", regexValueOperations);
        this.defineOperationParameterType(tr,"setValueOperation", setValueOperations);
        this.defineOperationParameterType(tr,"dateValueOperation", dateValueOperations);
    }

    private void defineOperationParameterType(TypeRegistry tr, String name, Set<AtomicConstraintType> operations){
        Transformer<String> transformer = this::extractConstraint;
        this.defineParameterType(tr, name, this.getHumanReadableOperationRegex(operations), transformer);
    }

    private void defineParameterType(TypeRegistry tr, String name, String regex) {
        Transformer<String> transformer = (fieldName) -> fieldName;
        this.defineParameterType(tr, name, regex, transformer);
    }

    private void defineParameterType(TypeRegistry tr, String name, String regex, Transformer<String> transformer) {
        tr.defineParameterType(new ParameterType<>(
            name,
            regex,
            String.class,
            transformer
        ));
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
        return "(" +
            types.stream()
            .map(act -> act.toString().replaceAll("([a-z])([A-Z]+)", "$1 $2").toLowerCase())
            .collect(Collectors.joining("|")) +
            ")";
    }

}
