Feature: User can specify that a number is of type decimal and can have varying numbers of decimal places

Background:
  Given the generation strategy is full
    And there is a field foo
    And foo is of type "decimal"
    And foo is anything but null

### Numeric Constraints

Scenario: Greater than constraint with decimal type produces valid decimals
  Given foo is greater than 10
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                     |
    | 10.00000000000000000001 |
    | 10.00000000000000000002 |
    | 10.00000000000000000003 |
    | 10.00000000000000000004 |
    | 10.00000000000000000005 |

Scenario: Greater than or equal to constraint with decimal type produces valid decimals
  Given foo is greater than or equal to 10
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                     |
    | 10                      |
    | 10.00000000000000000001 |
    | 10.00000000000000000002 |
    | 10.00000000000000000003 |
    | 10.00000000000000000004 |

#594 - Change to descending order?
Scenario: Less than or equal to constraint with decimal type produces valid decimals
  Given foo is less than or equal to 10
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                                         |
    | -100000000000000000000                      |
    | -99999999999999999999.99999999999999999999  |
    | -99999999999999999999.99999999999999999998  |
    | -99999999999999999999.99999999999999999997  |
    | -99999999999999999999.99999999999999999996  |

#594 - Change to descending order?
Scenario: Less than constraint with decimal type produces valid decimals
  Given foo is less than 10
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                                         |
    | -100000000000000000000                      |
    | -99999999999999999999.99999999999999999999  |
    | -99999999999999999999.99999999999999999998  |
    | -99999999999999999999.99999999999999999997  |
    | -99999999999999999999.99999999999999999996  |

Scenario: Equal to constraint with decimal type produces valid decimal
  Given foo is equal to 10.1
  Then the following data should be generated:
    | foo   |
    | 10.1  |

Scenario: In Set constraint with decimal type only produces valid decimals
  Given foo is in set:
    | 1   |
    | 1.1 |
  Then the following data should be generated:
    | foo |
    | 1   |
    | 1.1 |

Scenario: Granular to 2 d.p. with type decimal produces valid decimals with 2 d.p.
  Given foo is granular to 0.01
    And foo is greater than or equal to 0
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo   |
    | 0     |
    | 0.01  |
    | 0.02  |
    | 0.03  |
    | 0.04  |

Scenario: Granular to > 20 d.p. with type decimal produces data with 20 d.p.
  Given foo is granular to 0.00000000000000000000000000001
    And foo is greater than or equal to 0
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                    |
    | 0                      |
    | 0.00000000000000000001 |
    | 0.00000000000000000002 |
    | 0.00000000000000000003 |
    | 0.00000000000000000004 |
