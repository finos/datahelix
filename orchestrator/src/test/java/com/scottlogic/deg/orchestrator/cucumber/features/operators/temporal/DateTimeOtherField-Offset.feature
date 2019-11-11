Feature: running datetimes related to otherfield datetimes with an offset

  Background:
    Given the generation strategy is full
    And there is a non nullable field foo
    And foo has type "datetime"
    And there is a non nullable field bar
    And bar has type "datetime"
    And the combination strategy is exhaustive

Scenario: Running an "equalToField" constraint allows one date to be always equal to another with a positive offset
Given the generator can generate at most 3 rows
And there is a constraint:
"""
        {
          "field": "bar",
          "equalToField": "foo",
          "offset": 3,
          "offsetUnit": "days"
        }
      """
Then the following data should be generated:
| foo                      | bar                      |
| 0001-01-01T00:00:00.000Z | 0001-01-04T00:00:00.000Z |
| 0001-01-01T00:00:00.001Z | 0001-01-04T00:00:00.001Z |
| 0001-01-01T00:00:00.002Z | 0001-01-04T00:00:00.002Z |

Scenario: Running an "equalToField" constraint allows one date to be always equal to another with a negative offset
Given foo is after 2018-01-04T00:00:00.000Z
And the generator can generate at most 1 rows
And there is a constraint:
"""
        {
          "field": "foo",
          "equalToField": "bar",
          "offset": -3,
          "offsetUnit": "days"
        }
      """
Then the following data should be generated:
| foo                      | bar                      |
| 2018-01-04T00:00:00.001Z | 2018-01-07T00:00:00.001Z |
    # Results accomodate for the fact that the 5 working days include non-working days
Scenario: Running an "equalToField" constraint allows one date to be always equal to another plus a value in working days
Given the generator can generate at most 1 rows
And there is a constraint:
"""
        {
          "field": "bar",
          "equalToField": "foo",
          "offset": 5,
          "offsetUnit": "working days"
        }
      """
Then the following data should be generated:
| foo                      | bar                      |
| 0001-01-01T00:00:00.000Z | 0001-01-08T00:00:00.000Z |
    # Results accomodate for the fact that the 5 working days include non-working days
Scenario: Running an "equalToField" constraint allows one date to be always equal to another minus a value in working days
Given the generator can generate at most 1 rows
And foo is after or at 0001-01-01T00:00:00.000Z
And there is a constraint:
"""
        {
          "field": "foo",
          "equalToField": "bar",
          "offset": -5,
          "offsetUnit": "working days"
        }
      """
Then the following data should be generated:
| foo                      | bar                      |
| 0001-01-01T00:00:00.000Z | 0001-01-08T00:00:00.000Z |
