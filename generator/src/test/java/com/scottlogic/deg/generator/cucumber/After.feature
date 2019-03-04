#Generator expected to generate in ascending order where there are bracketing constraints
Feature: User can specify that a temporal date is lower than, but not equal to, a specified threshold

  Background:
    Given the generation strategy is full
    And there is a field foo
    And foo is of type "temporal"

#Related to #667 - Expected time increment is one millisecond
@ignore
Scenario: 'After' valid date is successful
  Given foo is after 2018-09-01T00:00:00.000
    And the generator can generate at most 4 rows
  Then the following data should be generated:
    | foo                     |
    | 2018-09-01T00:00:00.001 |
    | 2018-09-01T00:00:00.002 |
    | 2018-09-01T00:00:00.003 |
    | 2018-09-01T00:00:00.004 |

Scenario Outline: 'After' invalid datetime fails with error
  Given foo is after <dateValue>
  Then I am presented with an error message
    And no data is created
  Examples:
    | dateValue               |
    | 2018-09-32T00:00:00.000 |
    | 2018-09-01T25:00:00.000 |
    | 2018-13-01T00:00:00.000 |

#Issue #610 - Leap year date invalid
@ignore
Scenario: 'After' non-existent leap year date fails with error
  Given foo is after 2019-02-29T00:00:00.000
  Then I am presented with an error message
  And no data is created

  ### after ###

# Defect 611 "before run against a non contradicting before results in an error" related to this scenario
@ignore
Scenario: 'After' with a non-contradicting 'After' is successful
  Given foo is after 2019-01-01T00:00:00.000
    And foo is after 2019-02-01T00:00:00.000
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                     |
    | 2019-02-01T00:00:00.001 |
    | 2019-02-01T00:00:00.002 |
    | 2019-02-01T00:00:00.003 |
    | 2019-02-01T00:00:00.004 |
    | 2019-02-01T00:00:00.005 |

#Related to #667 - Expected time increment is one millisecond
@ignore
Scenario: 'After' with a non-contradicting 'Not After' is successful
  Given foo is after 2019-01-01T00:00:00.000
    And foo is anything but after 2020-01-01T00:00:00.000
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                     |
    | 2019-01-01T00:00:00.001 |
    | 2019-01-01T00:00:00.002 |
    | 2019-01-01T00:00:00.003 |
    | 2019-01-01T00:00:00.004 |
    | 2019-02-01T00:00:00.005 |

# Defect 611 "before run against a non contradicting before results in an error" related to this scenario
@ignore
Scenario: 'Not After' with a non-contradicting 'Not After' is successful
  Given foo is anything but after 2019-01-01T00:00:00.000
    And foo is anything but after 2020-01-01T00:00:00.000
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                     |
    | 2019-01-01T00:00:00.000 |
    | 2018-12-31T23:59:59.999 |
    | 2018-12-31T23:59:59.998 |
    | 2018-12-31T23:59:59.997 |
    | 2018-12-31T23:59:59.996 |


### Contradictions ###

#Defect #672 - Request for contradictory After and Not After should only generate null
@ignore
Scenario: 'After' with a contradicting 'Not After' generates null
  Given foo is after 2019-01-01T00:00:00.000
    And foo is anything but after 2019-01-01T00:00:00.000
  Then the following data should be generated:
    | foo                     |
    | null                    |



### afterOrAt ###

# Defect 611 "before run against a non contradicting before results in an error" related to this scenario
@ignore
Scenario: 'After' with a non-contradicting 'After Or At' is successful
Given foo is after 2019-01-01T00:00:00.000
  And foo is after or at 2019-02-01T00:00:00.000
  And the generator can generate at most 5 rows
Then the following data should be generated:
  | foo                     |
  | 2019-02-01T00:00:00.000 |
  | 2019-02-01T00:00:00.001 |
  | 2019-02-01T00:00:00.002 |
  | 2019-02-01T00:00:00.003 |
  | 2019-02-01T00:00:00.004 |

#Related to #667 - Expected time increment is one millisecond
@ignore
Scenario: 'After' with a non-contradicting 'Not After Or At' is successful
Given foo is after 2019-01-01T00:00:00.000
  And foo is anything but after or at 2020-01-01T00:00:00.000
  And the generator can generate at most 5 rows
Then the following data should be generated:
  | foo                     |
  | 2019-01-01T00:00:00.001 |
  | 2019-01-01T00:00:00.002 |
  | 2019-01-01T00:00:00.003 |
  | 2019-01-01T00:00:00.004 |
  | 2019-01-01T00:00:00.005 |

#Related to #667 - Expected time increment is one millisecond
@ignore
Scenario: 'Not After' with a non-contradicting 'After Or At' is successful
Given foo is anything but after 2020-01-01T00:00:00.000
  And foo is after or at 2019-01-01T00:00:00.000
  And the generator can generate at most 5 rows
Then the following data should be generated:
  | foo                     |
  | 2019-01-01T00:00:00.000 |
  | 2019-01-01T00:00:00.001 |
  | 2019-01-01T00:00:00.002 |
  | 2019-01-01T00:00:00.003 |
  | 2019-01-01T00:00:00.004 |

# Defect 611 "before run against a non contradicting before results in an error" related to this scenario
@ignore
Scenario: 'Not After' with a non-contradicting 'Not After Or At' is successful
Given foo is anything but after 2019-01-01T00:00:00.000
  And  foo is anything but after or at 2020-01-01T00:00:00.000
  And the generator can generate at most 5 rows
Then the following data should be generated:
  | foo                     |
  | 2019-01-01T00:00:00.000 |
  | 2018-12-31T23:59:59.999 |
  | 2018-12-31T23:59:59.998 |
  | 2018-12-31T23:59:59.997 |
  | 2018-12-31T23:59:59.996 |

### Contradictions ###

#Defect #672 - Request for contradictory After and Not After should only generate null
@ignore
Scenario: 'After' with a contradicting 'Not After Or At' only generates null
Given foo is after 2019-01-01T00:00:00.000
  And foo is anything but after or at 2019-01-01T00:00:00.000
  And the generator can generate at most 5 rows
Then the following data should be generated:
  | foo                     |
  | null                    |


### before ###

  #Related to #667 - Expected time increment is one millisecond
@ignore
Scenario: 'After' with a non-contradicting 'Before' is successful
Given foo is after 2019-01-01T00:00:00.000
  And foo is before 2020-01-01T00:00:00.000
  And the generator can generate at most 5 rows
Then the following data should be generated:
  | foo                     |
  | 2019-01-01T00:00:00.001 |
  | 2019-01-01T00:00:00.002 |
  | 2019-01-01T00:00:00.003 |
  | 2019-01-01T00:00:00.004 |
  | 2019-01-01T00:00:00.005 |

# Defect 611 "before run against a non contradicting before results in an error" related to this scenario
@ignore
Scenario: 'After' with a non-contradicting 'Not Before' is successful
Given foo is after 2019-01-01T00:00:00.000
  And foo is anything but before 2019-01-01T00:00:00.000
  And the generator can generate at most 5 rows
Then the following data should be generated:
  | foo                     |
  | 2019-01-01T00:00:00.001 |
  | 2019-01-01T00:00:00.002 |
  | 2019-01-01T00:00:00.003 |
  | 2019-01-01T00:00:00.004 |
  | 2019-01-01T00:00:00.005 |

  # Defect 611 "before run against a non contradicting before results in an error" related to this scenario
@ignore
Scenario: 'Not After' with a non-contradicting 'Before' is successful
Given foo is anything but after 2019-01-01T00:00:00.000
  And foo is before 2019-01-01T00:00:00.000
  And the generator can generate at most 5 rows
Then the following data should be generated:
  | foo                     |
  | 2018-12-31T23:59:59.999 |
  | 2018-12-31T23:59:59.998 |
  | 2018-12-31T23:59:59.997 |
  | 2018-12-31T23:59:59.996 |
  | 2018-12-31T23:59:59.995 |

 #Related to #667 - Expected time increment is one millisecond
@ignore
Scenario: 'Not After' with a non-contradicting 'Not Before' is successful
Given foo is anything but after 2019-01-01T00:00:00.000
  And foo is anything but before 2018-01-01T00:00:00.000
  And the generator can generate at most 5 rows
Then the following data should be generated:
  | foo                     |
  | 2018-01-01T00:00:00.000 |
  | 2018-01-01T00:00:00.001 |
  | 2018-01-01T00:00:00.002 |
  | 2018-01-01T00:00:00.003 |
  | 2018-01-01T00:00:00.004 |

### Contradictions ###

#Defect #673 - After: Request for contradictory After and Before should only generate null
@ignore
Scenario: 'Not After' with contradicting 'Not Before' only generates null
Given foo is anything but after 2019-01-01T00:00:00.000
  And foo is anything but before 2019-01-02T00:00:00.000
Then the following data should be generated:
    | foo                     |
    | null                    |

#Defect #673 - After: Request for contradictory After and Before should only generate null
@ignore
Scenario: 'After' with a contradicting 'Before' only generates null
Given foo is after 2019-01-02T00:00:00.000
  And foo is before 2019-01-01T00:00:00.000
Then the following data should be generated:
  | foo                     |
  | null                    |


### beforeOrAt ###

#Related to #667 - Expected time increment is one millisecond
@ignore
Scenario: 'After' with a non-contradicting 'Before' is successful
Given foo is after 2019-01-01T00:00:00.000
  And foo is before or at 2020-01-01T00:00:00.000
  And the generator can generate at most 5 rows
Then the following data should be generated:
  | foo                     |
  | 2019-01-01T00:00:00.001 |
  | 2019-01-01T00:00:00.002 |
  | 2019-01-01T00:00:00.003 |
  | 2019-01-01T00:00:00.004 |
  | 2019-01-01T00:00:00.005 |

  # Defect 611 "before run against a non contradicting before results in an error" related to this scenario
@ignore
Scenario: 'After' with a non-contradicting 'Not Before Or At' is successful
Given foo is after 2019-01-01T00:00:00.000
  And foo is anything but before or at 2019-01-02T00:00:00.000
  And the generator can generate at most 5 rows
Then the following data should be generated:
  | foo                     |
  | 2019-01-02T00:00:00.001 |
  | 2019-01-02T00:00:00.002 |
  | 2019-01-02T00:00:00.003 |
  | 2019-01-02T00:00:00.004 |
  | 2019-01-02T00:00:00.005 |

# Defect 611 "before run against a non contradicting before results in an error" related to this scenario
@ignore
Scenario: 'Not After' with a non-contradicting 'Before Or At' is successful
Given foo is anything but after 2019-01-02T00:00:00.000
  And  foo is before or at 2019-01-01T00:00:00.000
  And the generator can generate at most 5 rows
Then the following data should be generated:
  | foo                     |
  | 2019-01-01T00:00:00.000 |
  | 2018-12-31T23:59:59.999 |
  | 2018-12-31T23:59:59.998 |
  | 2018-12-31T23:59:59.997 |
  | 2018-12-31T23:59:59.996 |

#Related to #667 - Expected time increment is one millisecond
@ignore
Scenario: 'Not After' with a non-contradicting 'Not Before Or At' is successful
Given foo is anything but after 2019-01-01T00:00:00.000
  And foo is anything but before or at 2018-01-01T00:00:00.000
  And the generator can generate at most 5 rows
Then the following data should be generated:
  | foo                     |
  | 2018-01-01T00:00:00.001 |
  | 2018-01-01T00:00:00.002 |
  | 2018-01-01T00:00:00.003 |
  | 2018-01-01T00:00:00.004 |
  | 2018-01-01T00:00:00.005 |


### Contradictions ###

#Defect #673 - After: Request for contradictory After and Before should only generate null
@ignore
Scenario: 'After' with a contradicting 'Before Or At' only generates null
Given foo is after 2019-01-02T00:00:00.000
  And foo is before or at 2019-01-01T00:00:00.000
Then the following data should be generated:
  | foo                     |
  | null                    |

#Defect #673 - After: Request for contradictory After and Before should only generate null
@ignore
Scenario: 'Not After' with a contradicting not 'Before Or At' only generates null
Given foo is anything but after 2019-01-01T00:00:00.000
  And foo is anything but before or at 2019-01-02T00:00:00.000
Then the following data should be generated:
  | foo                     |
  | null                    |