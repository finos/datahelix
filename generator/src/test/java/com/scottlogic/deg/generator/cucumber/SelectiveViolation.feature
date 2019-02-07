Feature: User can set the generation strategy to violate mode and see test cases generated

Background:
  Given the generation strategy is interesting
  And the combination strategy is exhaustive
  And there is a field foo
  And the data requested is violating
  And the generator can generate at most 20 rows

  Scenario: Running the generator in violate but also saying not to violate the less than constraint is successful
    Given foo is less than 10
    When we do not violate any less than constraints
    Then the following data should be included in what is generated:
      | foo       |
      | 0         |
      | 9         |
      | null      |

  Scenario: Running the generator in violate mode with multiple fields and selective violation is successful
    Given foo is less than 10
    Given foo is of type "numeric"
    And there is a field bar
    And bar is in set:
      | "CCC" |
      | "DDD" |
    And we do not violate any in set constraints
    And we do not violate any of type constraints
    Then the following data should be included in what is generated:
      | foo        | bar   |
      | 10         | "CCC" |
      | 11         | "DDD" |
      | null       | null  |

  Scenario: Running the generator in violate but also saying not to violate an unrelated constraint is successful
    Given foo is less than 10
    When we do not violate any in set constraints
    Then the following data should be included in what is generated:
      | foo       |
      | 10        |
      | 11        |
      | null      |


