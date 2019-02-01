Feature: User can specify that a field is null or absent

  Background:
    Given the generation strategy is full
    And there is a field foo

  Scenario: Using the 'null' operator generates null values
    Given foo is null
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Negating the 'null' operator generates non-null values
    Given foo is in set:
      | "notNull" |
    And foo is anything but null
    Then the following data should be generated:
      | foo       |
      | "notNull" |

  Scenario: Negating the 'null' operator does not generate null values
    Given foo is null
    And foo is anything but null
    Then no data is created