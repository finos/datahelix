Feature: User can specify that a value either matches a regex in the form of an ISIN code

  Background:
      Given the generation strategy is interesting

  Scenario: User using matchingRegex operator to provide an exact set of values which represent an ISIN
    Given there is a field foo
    And foo is a valid "ISIN"
    And foo is anything but null
    Then the following data should be included in what is generated:
      | foo             |
      | "GB0000000009"  |
      | "GB00BBBBBB68"  |
      | "US0000000002"  |
      | "USAAAAAAAAA9"  |
    # Expand out expected values to inform design on additional interesting data to be generated

    Then the following data should not be included in what is generated:
      | foo             |
      | null |
      | "GB0"  |
      | "US000000000000"  |
      | "USUSUSUSUSUS"  |
