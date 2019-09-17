#### JUnit (Jupiter) 

This document outlines how unit tests should be written in JUnit.

```java
public class FixedFieldTests {
    @Test
    public void getFieldSpecForCurrentValue_fieldSpecNullAndCurrentValueNull_returnsFieldSpecWithSetRestrictionsAndNotNullRestrictions() {
        FixedField fixedField = new FixedField(
            new Field("Test"),
            Stream.of(new ArrayList<Object>() {{ add(null); }}),
            FieldSpec.Empty,
            mock(ReductiveDataGeneratorMonitor.class)
        );

        // Stream must be collected to set the current value
        fixedField.getStream().collect(Collectors.toList());
        FieldSpec fieldSpec = fixedField.getFieldSpecForCurrentValue(); 

        Assert.assertNotNull(fieldSpec.getSetRestrictions()); 
        Assert.assertNotNull(fieldSpec.getNullRestrictions()); 
        Assert.assertEquals(Nullness.MUST_NOT_BE_NULL, fieldSpec.getNullRestrictions().nullness);
    }
}
```

Things to remember:
* Use the JUnit Jupiter `org.junit.jupiter.api.Test` annotation
* Use the [_AAA_](https://medium.com/@pjbgf/title-testing-code-ocd-and-the-aaa-pattern-df453975ab80) pattern for test layout, readability is key
* Include `//Arrange`, `//Act`, `//Assert` if you think it helps, otherwise it is acceptable to use whitespace to define the groups
* If you `@Disable` tests, include a reason and a issue reference for resolution
* Ensure test fixtures are in the same (test) package as production code
* Ensure fixture class is named `<production-class-name>Tests`, like in example above
* Name each test appropriately, should be in the format `<method>_<scenario>_<expectation>`
* Ensure each test is focused to one aspect, don't test more than one thing at a time
* Use `@BeforeAll` or `@BeforeEach` annotations over methods that can reduce repetition for all/every test

### Examples

#### Good
```java
@Test
void equals_fieldSpecHasSetRestrictionsAndOtherObjectSetRestrictionsNull_returnsFalse() {
    FieldSpec fieldSpec = FieldSpec.Empty
        .withSetRestrictions(new SetRestrictions(null, null), null);

    boolean result = fieldSpec.equals(FieldSpec.Empty);

    Assert.assertFalse(
        "Expected that when the field spec has set restrictions and the other object set restrictions are null a false value should be returned but was true",
        result
    );
}
```

Why:
- Test is clearly named
- Groups are clearly deliniated, by whitespace, for the AAA structure
- There is only one thing being tested at a time
- The test method is short and can be seen on one screen at any time

#### Bad
```java
@TestFactory
Collection<DynamicTest> decisionTreeOptimiser_givenProfileInputs_resultEqualsProfileOutputs() {
    return doTest(new OptimiseTestStrategy());
}
```

Why:
- What is being tested, `doTest()` could do any number of things
- There is no arrange, act or assert groups
- There is no clarity over what is being asserted or clarity over what is being tested
