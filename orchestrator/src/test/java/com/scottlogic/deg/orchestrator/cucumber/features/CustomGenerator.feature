Feature: Generator use custom generators.

  Scenario: custom lorem ipsum generator can output rows
    Given the generation strategy is Random
    And the generator can generate at most 1 rows
    And there is a field foo
    And foo uses custom generator "lorem ipsum"
    Then the generator can generate at most 1 rows
