package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.schemas.v3.AtomicConstraintType;
import static com.scottlogic.deg.schemas.v3.AtomicConstraintType.*;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;

import java.util.*;
import java.util.stream.Collectors;

public class TypeRegistryConfiguration implements TypeRegistryConfigurer {

    private final Set numericValueOperations = new HashSet<AtomicConstraintType>(Arrays.asList(
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

    private final Set stringValueOperations = new HashSet<AtomicConstraintType>(Arrays.asList(
        ISEQUALTOCONSTANT,
        AVALID,
        ISAFTERCONSTANTDATETIME,
        ISAFTEROREQUALTOCONSTANTDATETIME,
        ISBEFORECONSTANTDATETIME,
        ISBEFOREOREQUALTOCONSTANTDATETIME,
        ISOFTYPE,
        FORMATTEDAS
    ));

    private final Set regexValueOperations = new HashSet<AtomicConstraintType>(Arrays.asList(
        MATCHESREGEX,
        CONTAINSREGEX
    ));

    private final Set setValueOperations = new HashSet<AtomicConstraintType>(Arrays.asList(
        ISINSET
    ));

    @Override
    public Locale locale(){
        return Locale.ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry tr) {
        this.defineParameterType(tr,"fieldVar", "^(.+)");
        this.defineParameterType(tr,"regex", "/(.+)/$");
        this.defineParameterType(tr,"set", "\\[((\"(.+)\")(, \"(.+)\")*)\\]");
        this.defineOperationParameterType(tr,"numericValueOperation", numericValueOperations);
        this.defineOperationParameterType(tr,"stringValueOperation", stringValueOperations);
        this.defineOperationParameterType(tr,"regexValueOperation", regexValueOperations);
        this.defineOperationParameterType(tr,"setValueOperation", setValueOperations);
    }

    private void defineOperationParameterType(TypeRegistry tr, String name, Set<AtomicConstraintType> operations){
        Transformer<String> transformer = (gherkinConstraint) -> extractConstraint(gherkinConstraint);
        this.defineParameterType(tr, name, this.getHumanReadableOperationRegex(operations), transformer);
    }

    private void defineParameterType(TypeRegistry tr, String name, String regex) {
        Transformer<String> transformer = (fieldName) -> fieldName;
        this.defineParameterType(tr, name, regex, transformer);
    }

    private void defineParameterType(TypeRegistry tr, String name, String regex, Transformer transformer) {
        tr.defineParameterType(new ParameterType<String>(
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
