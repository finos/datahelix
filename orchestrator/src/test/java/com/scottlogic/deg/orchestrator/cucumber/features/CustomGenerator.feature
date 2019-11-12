Feature: Generator use custom generators.

  Scenario: custom lorem ipsum generator can output rows
    Given the generation strategy is Random
    And the generator can generate at most 1 rows
    And there is a non nullable field foo
    And foo uses custom generator "lorem ipsum"
    Then the generator can generate at most 1 rows
