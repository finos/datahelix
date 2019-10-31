Feature: running datetimes related to otherfield datetimes with granularities

  Background:
    Given the generation strategy is full
    And there is a non nullable field foo
    And foo has type "datetime"
    And foo is after 2000-01-01T00:00:01.000Z
    And there is a non nullable field bar
    And bar has type "datetime"
    And the combination strategy is exhaustive
    And the generator can generate at most 1 rows
    And there is a constraint:
    """
        {
          "field": "bar",
          "afterField": "foo"
        }
      """

    Scenario: Foo and bar both granular to minutes then bar is 1 minute after foo
      Given foo is granular to "minutes"
      And bar is granular to "minutes"
      Then the following data should be generated:
        | foo                | bar                      |
        | 2000-01-01T00:01:00.000Z | 2000-01-01T00:02:00.000Z |

    Scenario: Foo granular to minutes and bar is granular to days then bar is midnight the day following foo
      Given foo is granular to "minutes"
      And bar is granular to "days"
      Then the following data should be generated:
        | foo                | bar                      |
        | 2000-01-01T00:01:00.000Z | 2000-01-02T00:00:00.000Z |

    Scenario: Foo granular to days and bar granular to minutes then foo rounds up to midnight the next day and bar is midnight the day after
      Given foo is granular to "days"
      And bar is granular to "minutes"
      Then the following data should be generated:
        | foo                | bar                      |
        | 2000-01-02T00:00:00.000Z | 2000-01-03T00:00:00.000Z |

    Scenario: Foo and bar both granular to days then foo rounds up to midnight the next day and bar is midnight the day after
      Given foo is granular to "days"
      And bar is granular to "days"
      Then the following data should be generated:
        | foo                | bar                      |
        | 2000-01-02T00:00:00.000Z | 2000-01-03T00:00:00.000Z |
