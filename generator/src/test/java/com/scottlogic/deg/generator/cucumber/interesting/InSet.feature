Feature: User can generate interesting values for values that are in a set

Background:
    Given the generation strategy is interesting

Scenario: Running an 'inSet' request that includes 4 strings with alphabetic characters and not null should return 2 values
    Given there is a field foo
    And foo is anything but null
    And foo is in set:
      | "ABC" |
      | "DEF" |
      | "GHI" |
      | "JKL" |
    Then the following data should be generated:
      | "foo" |
      | "ABC" |
      | "DEF" |

Scenario: Running an 'inSet' request that includes 4 strings with alphabetic characters should return 2 values and null
    Given there is a field foo
    And foo is in set:
      | "ABC" |
      | "DEF" |
      | "GHI" |
      | "JKL" |
    Then the following data should be generated:
      | foo   |
      | null  |
      | "ABC" |
      | "DEF" |

Scenario: Running an 'inSet' request that includes only a single string with alphabetic characters should return 1 value and null
    Given there is a field foo
    And foo is in set:
      | "Test" |
    Then the following data should be generated:
      | foo    |
      | null   |
      | "Test" |

Scenario: Running an 'inSet' request that includes 4 numeric values and not null should return 2 values
    Given there is a field foo
    And foo is anything but null
    And foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    Then the following data should be generated:
      | foo |
      | 20  |
      | 40  |

Scenario: Running an 'inSet' request that includes 4 numeric values should return 2 values and null
    Given there is a field foo
    And foo is in set:
      | 100 |
      | 2000 |
      | 300 |
      | 550 |
    Then the following data should be generated:
      | foo  |
      | null |
      | 100  |
      | 2000 |

 Scenario: Running an 'inSet' request that includes 4 decimal numeric values should return 2 decimal values and null
    Given there is a field foo
    And foo is in set:
      | 100.05   |
      | 253.0054 |
      | 10.8     |
      | 0.00382  |
    Then the following data should be generated:
      | foo      |
      | null     |
      | 10.8     |
      | 253.0054 |

Scenario: Running an 'inSet' request that includes 4 temporal values should return 2 temporal values and null
    Given there is a field foo
    And foo is in set:
      | "2010-01-01T00:00:00.000" |
      | "2012-01-01T00:01:00.000" |
      | "2011-01-02T00:00:00.000" |
      | "2012-01-02T01:10:00.000" |
    Then the following data should be generated:
      | foo                       |
      | null                      |
      | "2010-01-01T00:00:00.000" |
      | "2012-01-01T00:01:00.000" |

Scenario: Running an 'inSet' request that has multiple special characters should return 2 values and null
    Given there is a field foo
    And foo is in set:
      | "Cyrillic text: Тхис ис Тест Нумбер 01" |
      | "Japanese text: これはテスト番号2です"   |
      | "Korean text: 이것은 시험 번호 3입니다"  |
    Then the following data should be generated:
      | foo |
      | null |
      | "Japanese text: これはテスト番号2です"   |
      | "Korean text: 이것은 시험 번호 3입니다"  |

Scenario: Running an 'inSet' request that has multiple set constraints should return 2 values from each set and null
    Given there is a field foo
    And there is a constraint:
      """
      {
        "anyOf": [
          { "field": "foo", "is": "inSet", "values": [1, 2, 3, 4] },
          { "field": "foo", "is": "inSet", "values": [5, 6, 7, 8] }
        ]
      }
      """
    Then the following data should be included in what is generated:
      | foo  |
      | null |
      | 1    |
      | 2    |
      | 5    |
      | 6    |

Scenario: Running an 'inSet' request that has multiple fields with sets should return 2 values from each field set
    Given the following fields exist:
      | foo |
      | bar |
    And foo is anything but null
    And bar is anything but null
    And foo is in set:
      | "A" |
      | "B" |
      | "C" |
      | "D" |
    And bar is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    Then the following data should be generated:
      | foo | bar |
      | "A" | 1 |
      | "A" | 2 |
      | "B" | 1 |