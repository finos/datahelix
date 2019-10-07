Feature: User can specify that a field is null or absent

  Background:
    Given the generation strategy is full
    And there is a field foo

### alone ###

  Scenario: Using the 'null' operator generates null values
    Given foo is null
    And foo has type "string"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Negating the 'null' operator generates non-null values
    Given foo is in set:
      | "string" |
    And foo has type "string"
    And foo is anything but null
    Then the following data should be generated:
      | foo      |
      | "string" |

  Scenario: Negating the 'null' operator does not generate null values
    Given foo is null
    And foo is anything but null
    And foo has type "string"
    Then no data is created

### null ###

  Scenario: 'Null' with 'null' is successful
    Given foo is null
    And foo is null
    And foo has type "string"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Not 'null' with  not 'null' is successful
    Given foo is anything but null
    And foo is anything but null
    And foo has type "string"
    And foo is equal to "a"
    Then the following data should be generated:
      | foo |
      | "a" |

### ofType ###

  Scenario Outline: Not 'null' with 'ofType' is successful
    Given foo is anything but null
    And foo has type <type>
    And foo is equal to <typeValue>
    Then the following data should be generated:
      | foo         |
      | <typeValue> |
    Examples:
      | type       | typeValue                |
      | "string"   | "a"                      |
      | "decimal"  | 1.1                      |
      | "integer"  | 1                        |
      | "datetime" | 2019-01-01T00:00:00.000Z |

  Scenario Outline: 'Null' with 'ofType' emits null
    Given foo is null
    And foo has type <type>
    Then the following data should be generated:
      | foo  |
      | null |
    Examples:
      | type       |
      | "string"   |
      | "integer"  |
      | "decimal"  |
      | "datetime" |

### matchingRegex ###

  Scenario: Not 'null' with a non-contradicting 'matchingRegex' is successful
    Given foo is anything but null
    And foo has type "string"
    And foo is matching regex /[a]{1}/
    Then the following data should be generated:
      | foo |
      | "a" |

  Scenario: 'Null' with 'matchingRegex' emits null
    Given foo is null
    And foo has type "string"
    And foo is matching regex /[a]{1}/
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Null' with not 'matchingRegex' emits null
    Given foo is null
    And foo has type "string"
    And foo is anything but matching regex /[a]{1}/
    Then the following data should be generated:
      | foo  |
      | null |

### containingRegex ###

  Scenario: Not 'null' with a non contradicting 'containingRegex' is successful
    Given foo is anything but null
    And foo has type "string"
    And foo is containing regex /[a]{1}/
    And foo is in set:
      | "a"  |
      | "ab" |
      | "1"  |
    Then the following data should be generated:
      | foo  |
      | "a"  |
      | "ab" |

  Scenario: 'Null' with a contradicting 'containingRegex' should only generate null
    Given foo is null
    And foo has type "string"
    And foo is containing regex /[a]{1}/
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Null' with a contradicting not 'containingRegex' emits null
    Given foo is null
    And foo has type "string"
    And foo is anything but containing regex /[a]{1}/
    Then the following data should be generated:
      | foo  |
      | null |

### ofLength ###

  Scenario: Not 'null' with a non contradicting 'ofLength' should be successful
    Given foo is anything but null
    And foo has type "string"
    And foo is of length 0
    Then the following data should be generated:
      | foo |
      | ""  |

  Scenario: 'Null' with a contradicting 'ofLength' should only generate null
    Given foo is null
    And foo has type "string"
    And foo is of length 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Null' with a contradicting not 'ofLength' should only generate null
    Given foo is null
    And foo has type "string"
    And foo is anything but of length 1
    Then the following data should be generated:
      | foo  |
      | null |

### longerThan ###

  Scenario: Not 'null' with a non contradicting 'longerThan' should be successful
    Given foo is anything but null
    And foo has type "string"
    And foo is longer than 0
    And foo is in set:
      | ""  |
      | "a" |
    Then the following data should be generated:
      | foo |
      | "a" |

  Scenario: 'Null' with a contradicting 'longerThan' should only generate null
    Given foo is null
    And foo has type "string"
    And foo is longer than 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Null' with a contradicting not 'longerThan' should only generate null
    Given foo is null
    And foo has type "string"
    And foo is anything but longer than 1
    Then the following data should be generated:
      | foo  |
      | null |

### shorterThan ###

  Scenario: Not 'null' with a non contradicting 'shorterThan' should be successful
    Given foo is anything but null
    And foo has type "string"
    And foo has type "string"
    And foo is shorter than 1
    Then the following data should be generated:
      | foo |
      | ""  |

  Scenario: 'Null' with a contradicting 'shorterThan' should only generate null
    Given foo is null
    And foo has type "string"
    And foo is shorter than 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Null' with a contradicting not 'shorterThan' should only generate null
    Given foo is null
    And foo has type "string"
    And foo is anything but shorter than 1
    Then the following data should be generated:
      | foo  |
      | null |

### Financial data types ###

  Scenario: Not null combined with an ISIN constraint generates valid ISINs
    Given foo is anything but null
    And foo has type "string"
    And foo has type "ISIN"
    And foo is in set:
      | "GB0002634946" |
    Then the following data should be generated:
      | foo            |
      | "GB0002634946" |

  Scenario: Null combined with an ISIN constraint generates null
    Given foo is null
    And foo has type "string"
    And foo has type "ISIN"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Not null combined with a SEDOL constraint generates valid SEDOLs
    Given foo is anything but null
    And foo has type "string"
    And foo has type "SEDOL"
    And foo is in set:
      | "0263494" |
    Then the following data should be generated:
      | foo       |
      | "0263494" |

  Scenario: Null combined with a SEDOL constraint generates null
    Given foo is null
    And foo has type "string"
    And foo has type "SEDOL"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Not null combined with a CUSIP constraint generates valid CUSIPs
    Given foo is anything but null
    And foo has type "string"
    And foo has type "CUSIP"
    And foo is in set:
      | "38259P508" |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |

  Scenario: Null combined with a CUSIP constraint generates null
    Given foo is null
    And foo has type "string"
    And foo has type "CUSIP"
    Then the following data should be generated:
      | foo  |
      | null |

### greaterThan ###

  Scenario: Not 'null' with a non contradicting 'greaterThan' should be successful
    Given foo is anything but null
    And foo has type "decimal"
    And foo is greater than 1
    And the generator can generate at most 2 rows
    Then the following data should be generated:
      | foo                    |
      | 1.00000000000000000001 |
      | 1.00000000000000000002 |

  Scenario: 'Null' with a contradicting 'greaterThan' should only generate null
    Given foo is null
    And foo has type "integer"
    And foo is greater than 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Null' with a contradicting not 'greaterThan' should only generate null
    Given foo is null
    And foo has type "decimal"
    And foo is anything but greater than 1
    Then the following data should be generated:
      | foo  |
      | null |

### greaterThanOrEqualTo ###

  Scenario: Not 'null' with a non contradicting 'greaterThanOrEqualTo' should be successful
    Given foo is anything but null
    And foo has type "decimal"
    And foo is greater than or equal to 1
    And the generator can generate at most 2 rows
    Then the following data should be generated:
      | foo                    |
      | 1                      |
      | 1.00000000000000000001 |

  Scenario: 'Null' with a contradicting 'greaterThanOrEqualTo' should only generate null
    Given foo is null
    And foo has type "decimal"
    And foo is greater than or equal to 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Null' with a contradicting not 'greaterThanOrEqualTo' should only generate null
    Given foo is null
    And foo has type "decimal"
    And foo is anything but greater than or equal to 1
    Then the following data should be generated:
      | foo  |
      | null |

### lessThan ###

  Scenario: 'Null' with a contradicting 'lessThan' should only generate null
    Given foo is null
    And foo has type "decimal"
    And foo is less than 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Null' with a contradicting not 'lessThan' should only generate null
    Given foo is null
    And foo has type "decimal"
    And foo is anything but less than 1
    Then the following data should be generated:
      | foo  |
      | null |

### lessThanOrEqualTo ###

  Scenario: 'Null' with a contradicting 'lessThanOrEqualTo' should only generate null
    Given foo is null
    And foo has type "decimal"
    And foo is less than or equal to 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Null' with a contradicting not 'lessThanOrEqualTo' should only generate null
    Given foo is null
    And foo has type "decimal"
    And foo is anything but less than or equal to 1
    Then the following data should be generated:
      | foo  |
      | null |

### granularTo ###

  Scenario: Not 'null' with a non contradicting 'granularTo' should be successful
    Given foo is anything but null
    And foo has type "decimal"
    And foo is granular to 1
    And foo is in set:
      | 1   |
      | 1.1 |
    Then the following data should be generated:
      | foo |
      | 1   |

  Scenario: 'Null' with a contradicting 'granularTo' should only generate null
    Given foo is null
    And foo has type "decimal"
    And foo is granular to 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Null' with a contradicting not 'granularTo' should only generate null
    Given foo is null
    And foo has type "decimal"
    And foo is anything but granular to 1
    Then the following data should be generated:
      | foo  |
      | null |

### after ###
  Scenario: Not 'null' with a non contradicting 'after' should be successful
    Given foo is anything but null
    And foo has type "datetime"
    And foo is after 2019-01-01T00:00:00.000Z
    And the generator can generate at most 2 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |

  Scenario: 'Null' with a contradicting 'after' should only generate null
    Given foo is null
    And foo has type "datetime"
    And foo is after 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Null' with a contradicting not 'after' should only generate null
    Given foo is null
    And foo has type "datetime"
    And foo is after 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

### afterOrAt ###

  Scenario: Not 'null' with a non contradicting 'afterOrAt' should be successful
    Given foo is anything but null
    And foo has type "datetime"
    And foo is after or at 2019-01-01T00:00:00.000Z
    And the generator can generate at most 2 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.001Z |

  Scenario: 'Null' with a contradicting 'afterOrAt' should only generate null
    Given foo is null
    And foo has type "datetime"
    And foo is after or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Null' with a contradicting not 'afterOrAt' should only generate null
    Given foo is null
    And foo has type "datetime"
    And foo is after or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

### before ###

  @ignore #594 - Reverse order of value generation when only upper-bound operators are provided
  Scenario: Not 'null' with a non contradicting 'before' should be successful
    Given foo is anything but null
    And foo has type "datetime"
    And foo is before 2019-01-01T00:00:00.003Z
    And the generator can generate at most 2 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.002Z |
      | 2019-01-01T00:00:00.001Z |

  Scenario: 'Null' with a contradicting 'before' should only generate null
    Given foo is null
    And foo has type "datetime"
    And foo is before 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Null' with a contradicting not 'before' should only generate null
    Given foo is null
    And foo has type "datetime"
    And foo is before 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

### beforeOrAt ###

  @ignore #594 - Reverse order of value generation when only upper-bound operators are provided
  Scenario: Not 'null' with a non contradicting 'beforeOrAt' should be successful
    Given foo is anything but null
    And foo has type "datetime"
    And foo is before or at 2019-01-01T00:00:00.002Z
    And the generator can generate at most 2 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.002Z |
      | 2019-01-01T00:00:00.001Z |

  Scenario: 'Null' with a contradicting 'beforeOrAt' should only generate null
    Given foo is null
    And foo has type "datetime"
    And foo is before or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Null' with a contradicting not 'beforeOrAt' should only generate null
    Given foo is null
    And foo has type "datetime"
    And foo is before or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |
