Feature: User can specify that a number is of type integer and does not have any decimal places

  Background:
    Given the generation strategy is full
    And there is a field foo
    And foo is of type "integer"
    And foo is anything but null

### Numeric Constraints

Scenario: Greater than constraint with integer type produces valid integers
  Given foo is greater than 10
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo |
    | 11  |
    | 12  |
    | 13  |
    | 14  |
    | 15  |

Scenario: Greater than or equal to constraint with integer type produces valid integers
  Given foo is greater than or equal to 10
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo |
    | 10  |
    | 11  |
    | 12  |
    | 13  |
    | 14  |

#594 - Change this to descending order?
Scenario: Less than or equal to constraint with integer type produces valid integers
  Given foo is less than or equal to 10
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                     |
    | -100000000000000000000  |
    | -99999999999999999999   |
    | -99999999999999999998   |
    | -99999999999999999997   |
    | -99999999999999999996   |

#594 - Change this to descending order?
Scenario: Less than constraint with integer type produces valid integers
  Given foo is less than 10
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                     |
    | -100000000000000000000  |
    | -99999999999999999999   |
    | -99999999999999999998   |
    | -99999999999999999997   |
    | -99999999999999999996   |

Scenario: Equal to constraint with integer type produces valid integer
  Given foo is equal to 10
  Then the following data should be generated:
    | foo |
    | 10  |

Scenario: Equal to constraint with integer type rejects invalid integer (decimal)
  Given foo is equal to 10.1
  Then no data is created

@ignore #677 - Granularity ignored when generating from set
Scenario: In Set constraint with integer type only produces valid integers
  Given foo is in set:
    | 1   |
    | 1.1 |
  Then the following data should be generated:
    | foo |
    | 1   |

@ignore #677 - Granularity ignored when generating from set
Scenario: Contradictory granular to constraint with integer type only produces valid integers
  Given foo is in set:
    | 1   |
    | 1.1 |
    And foo is granular to 0.1
  Then the following data should be generated:
    | foo |
    | 1   |
    
Scenario: Granular to constraint does not affect integer
  Given foo is greater than 10
    And the generator can generate at most 5 rows
    And foo is granular to 0.01
  Then the following data should be generated:
    | foo |
    | 11  |
    | 12  |
    | 13  |
    | 14  |
    | 15  |
