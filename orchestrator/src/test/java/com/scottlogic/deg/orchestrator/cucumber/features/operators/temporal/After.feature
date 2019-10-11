Feature: User can specify that a datetime date is after, but not equal to, a specified datetime

  Background:
    Given the generation strategy is full
    And there is a field foo
    And foo has type "datetime"

  Scenario: 'After' valid date is successful for a single row
    Given foo is after 2018-09-01T00:00:00.000Z
    And the generator can generate at most 1 rows
    Then the following data should be generated:
      | foo                      |
      | 2018-09-01T00:00:00.001Z |

  Scenario: 'After' valid date is successful
    Given foo is after 2018-09-01T00:00:00.000Z
    And the generator can generate at most 4 rows
    Then the following data should be generated:
      | foo                      |
      | 2018-09-01T00:00:00.001Z |
      | 2018-09-01T00:00:00.002Z |
      | 2018-09-01T00:00:00.003Z |
      | 2018-09-01T00:00:00.004Z |

  Scenario Outline: 'After' invalid datetime fails with error
    Given foo is after <dateValue>
    Then the profile is invalid because "Field \[foo\]: Date string '\d{4}-\d{2}-\d{2}T\d{2}:00:00.000Z' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS\[Z\] between \(inclusive\) 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z"
    And no data is created
    Examples:
      | dateValue                |
      | 2018-09-32T00:00:00.000Z |
      | 2018-09-01T25:00:00.000Z |
      | 2018-13-01T00:00:00.000Z |

  Scenario: 'After' non-existent leap year date fails with error
    Given foo is after 2019-02-29T00:00:00.000Z
    Then the profile is invalid because "Field \[foo\]: Date string '2019-02-29T00:00:00.000Z' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS\[Z\] between \(inclusive\) 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z"
    And no data is created

  ### after ###

  Scenario: 'After' with a non-contradicting 'After' is successful
    Given foo is after 2019-01-01T00:00:00.000Z
    And foo is after 2019-02-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-02-01T00:00:00.001Z |
      | 2019-02-01T00:00:00.002Z |
      | 2019-02-01T00:00:00.003Z |
      | 2019-02-01T00:00:00.004Z |
      | 2019-02-01T00:00:00.005Z |

  Scenario: 'After' with a non-contradicting 'Not After' is successful
    Given foo is after 2019-01-01T00:00:00.000Z
    And foo is anything but after 2020-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
      | 2019-01-01T00:00:00.003Z |
      | 2019-01-01T00:00:00.004Z |
      | 2019-01-01T00:00:00.005Z |

  Scenario: 'After' with a contradicting 'Not After' generates null
    Given foo is after 2019-01-01T00:00:00.000Z
    And foo is anything but after 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

### afterOrAt ###

  Scenario: 'After' with a non-contradicting 'After Or At' is successful
    Given foo is after 2019-01-01T00:00:00.000Z
    And foo is after or at 2019-02-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-02-01T00:00:00.000Z |
      | 2019-02-01T00:00:00.001Z |
      | 2019-02-01T00:00:00.002Z |
      | 2019-02-01T00:00:00.003Z |
      | 2019-02-01T00:00:00.004Z |

  Scenario: 'After' with a non-contradicting 'Not After Or At' is successful
    Given foo is after 2019-01-01T00:00:00.000Z
    And foo is anything but after or at 2020-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
      | 2019-01-01T00:00:00.003Z |
      | 2019-01-01T00:00:00.004Z |
      | 2019-01-01T00:00:00.005Z |

  Scenario: 'Not After' with a non-contradicting 'After Or At' is successful
    Given foo is anything but after 2020-01-01T00:00:00.000Z
    And foo is after or at 2019-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
      | 2019-01-01T00:00:00.003Z |
      | 2019-01-01T00:00:00.004Z |

  Scenario: 'After' with a contradicting 'Not After Or At' only generates null
    Given foo is after 2019-01-01T00:00:00.000Z
    And foo is anything but after or at 2019-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo  |
      | null |

### before ###

  Scenario: 'After' with a non-contradicting 'Before' is successful
    Given foo is after 2019-01-01T00:00:00.000Z
    And foo is before 2020-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
      | 2019-01-01T00:00:00.003Z |
      | 2019-01-01T00:00:00.004Z |
      | 2019-01-01T00:00:00.005Z |

  Scenario: 'After' with a non-contradicting 'Not Before' is successful
    Given foo is after 2019-01-01T00:00:00.000Z
    And foo is anything but before 2019-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
      | 2019-01-01T00:00:00.003Z |
      | 2019-01-01T00:00:00.004Z |
      | 2019-01-01T00:00:00.005Z |

  Scenario: 'Not After' with a non-contradicting 'Not Before' is successful
    Given foo is anything but after 2019-01-01T00:00:00.000Z
    And foo is anything but before 2018-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2018-01-01T00:00:00.000Z |
      | 2018-01-01T00:00:00.001Z |
      | 2018-01-01T00:00:00.002Z |
      | 2018-01-01T00:00:00.003Z |
      | 2018-01-01T00:00:00.004Z |

  Scenario: 'Not After' with contradicting 'Not Before' only generates null
    Given foo is anything but after 2019-01-01T00:00:00.000Z
    And foo is anything but before 2019-01-02T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'After' with a contradicting 'Before' only generates null
    Given foo is after 2019-01-02T00:00:00.000Z
    And foo is before 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

### beforeOrAt ###

  Scenario: 'After' with a non-contradicting 'Before' is successful
    Given foo is after 2019-01-01T00:00:00.000Z
    And foo is before or at 2020-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
      | 2019-01-01T00:00:00.003Z |
      | 2019-01-01T00:00:00.004Z |
      | 2019-01-01T00:00:00.005Z |

  Scenario: 'After' with a non-contradicting 'Not Before Or At' is successful
    Given foo is after 2019-01-01T00:00:00.000Z
    And foo is anything but before or at 2019-01-02T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-02T00:00:00.001Z |
      | 2019-01-02T00:00:00.002Z |
      | 2019-01-02T00:00:00.003Z |
      | 2019-01-02T00:00:00.004Z |
      | 2019-01-02T00:00:00.005Z |

  Scenario: 'Not After' with a non-contradicting 'Not Before Or At' is successful
    Given foo is anything but after 2019-01-01T00:00:00.000Z
    And foo is anything but before or at 2018-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2018-01-01T00:00:00.001Z |
      | 2018-01-01T00:00:00.002Z |
      | 2018-01-01T00:00:00.003Z |
      | 2018-01-01T00:00:00.004Z |
      | 2018-01-01T00:00:00.005Z |

  Scenario: 'After' with a contradicting 'Before Or At' only generates null
    Given foo is after 2019-01-02T00:00:00.000Z
    And foo is before or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Not After' with a contradicting not 'Before Or At' only generates null
    Given foo is anything but after 2019-01-01T00:00:00.000Z
    And foo is anything but before or at 2019-01-02T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'after' run with maximum possible date should only generate null
    Given foo is after 9999-12-31T23:59:59.999Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running a 'after' request that specifies the highest valid system date should be unsuccessful
    Given foo is after 10000-01-01T00:00:00.000Z
    Then the profile is invalid because "Field \[foo\]: Dates must be between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z"
