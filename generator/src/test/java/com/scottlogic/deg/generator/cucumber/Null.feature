Feature: User can specify that a field is null or absent

  Background:
    Given the generation strategy is full
    And there is a field foo

  Scenario: Using the 'null' operator generates null values
    Given foo is null
    Then the following data should be generated:
      | foo  |
      | null |


  @ignore @bug
  Scenario: Negating the 'null' operator generates non-null values
    Given foo is in set:
      | "notNull" |
      | null      |
    And foo is anything but null
    Then the following data should not be included in what is generated:
      | foo  |
      | null |
