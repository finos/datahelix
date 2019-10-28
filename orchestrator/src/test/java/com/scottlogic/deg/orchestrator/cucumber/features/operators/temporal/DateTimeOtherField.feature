Feature: running datetimes related to otherfield datetimes

  Background:
    Given the generation strategy is full
    And there is a non nullable field foo
    And foo has type "datetime"
    And there is a non nullable field bar
    And bar has type "datetime"
    And the combination strategy is exhaustive

  Scenario: Running an "afterField" constraint allows one date to be always later than another
    Given foo is after 2018-09-01T00:00:00.000Z
    And bar is before 2018-09-01T00:00:00.004Z
    And bar is after field foo
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-09-01T00:00:00.001Z | 2018-09-01T00:00:00.002Z |
      | 2018-09-01T00:00:00.001Z | 2018-09-01T00:00:00.003Z |
      | 2018-09-01T00:00:00.002Z | 2018-09-01T00:00:00.003Z |

  Scenario: Running an "afterOrAtField" constraint allows one date to be always later than or equal to another
    Given foo is after 2018-09-01T00:00:00.000Z
    And bar is before 2018-09-01T00:00:00.004Z
    And bar is after or at field foo
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-09-01T00:00:00.001Z | 2018-09-01T00:00:00.001Z |
      | 2018-09-01T00:00:00.001Z | 2018-09-01T00:00:00.002Z |
      | 2018-09-01T00:00:00.001Z | 2018-09-01T00:00:00.003Z |
      | 2018-09-01T00:00:00.002Z | 2018-09-01T00:00:00.002Z |
      | 2018-09-01T00:00:00.002Z | 2018-09-01T00:00:00.003Z |
      | 2018-09-01T00:00:00.003Z | 2018-09-01T00:00:00.003Z |

  Scenario: Running a "beforeField" constraint allows one date to be always earlier than another
    Given the generator can generate at most 3 rows
    And bar is before 0001-01-01T00:00:00.003Z
    And foo is before field bar
    Then the following data should be generated:
      | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.002Z |
      | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.002Z |

  Scenario: Running a "beforeOrAtField" constraint allows one date to be always earlier than or equal to another
    And bar is before 0001-01-01T00:00:00.003Z
    And foo is before or at field bar
    Then the following data should be generated:
      | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.000Z |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.002Z |
      | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.001Z |
      | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.002Z |
      | 0001-01-01T00:00:00.002Z | 0001-01-01T00:00:00.002Z |


  Scenario: Running an "beforeField" constraint allows one date to be always earlier than another with a positive offset
    Given the generator can generate at most 1 rows
    And foo is after 2000-01-01T00:00:00.000Z
    And bar is after 1999-12-27T23:59:59.999Z
    And there is a constraint:
      """
        {
          "field": "bar",
          "beforeField": "foo",
          "offset": 3,
          "offsetUnit": "days"
        }
      """
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2000-01-01T00:00:00.001Z | 1999-12-28T00:00:00.000Z |

  Scenario: Running an "equalToField" constraint allows one date to be always equal to another
    Given foo is equal to 2018-09-01T00:00:00.000Z
    And the generator can generate at most 1 rows
    And foo is equal to field bar
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-09-01T00:00:00.000Z | 2018-09-01T00:00:00.000Z |

