Feature: running datetimes related to otherfield datetimes

  Background:
    Given the generation strategy is full
    And there is a field foo
    And foo has type "datetime"
    And foo is anything but null
    And there is a field bar
    And bar has type "datetime"
    And bar is anything but null
    And the combination strategy is exhaustive

  Scenario: Running an "afterField" constraint allows one date to be always later than another
    Given foo is after 2018-09-01T00:00:00.000Z
    And bar is before 2018-09-01T00:00:00.004Z
    And there is a constraint:
    """
        {
          "field": "bar",
          "is": "after",
          "otherField": "foo"
        }
    """
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-09-01T00:00:00.001Z | 2018-09-01T00:00:00.002Z |
      | 2018-09-01T00:00:00.001Z | 2018-09-01T00:00:00.003Z |
      | 2018-09-01T00:00:00.002Z | 2018-09-01T00:00:00.003Z |


  Scenario: Running an "afterOrAtField" constraint allows one date to be always later than or equal to another
    Given foo is after 2018-09-01T00:00:00.000Z
    And bar is before 2018-09-01T00:00:00.004Z
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "afterOrAt",
          "otherField": "foo"
        }
      """
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
    And there is a constraint:
      """
        {
          "field": "foo",
          "is": "before",
          "otherField": "bar"
        }
      """
    Then the following data should be generated:
      | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.002Z |
      | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.002Z |


  Scenario: Running a "beforeOrAtField" constraint allows one date to be always earlier than or equal to another
    And bar is before 0001-01-01T00:00:00.003Z
    And there is a constraint:
      """
        {
          "field": "foo",
          "is": "beforeOrAt",
          "otherField": "bar"
        }
      """
    Then the following data should be generated:
      | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.000Z |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.002Z |
      | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.001Z |
      | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.002Z |
      | 0001-01-01T00:00:00.002Z | 0001-01-01T00:00:00.002Z |


  Scenario: Running an "equalToField" constraint allows one date to be always equal to another
    Given foo is equal to 2018-09-01T00:00:00.000Z
    And the generator can generate at most 1 rows
    And there is a constraint:
      """
        {
          "field": "foo",
          "is": "equalTo",
          "otherField": "bar"
        }
      """
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-09-01T00:00:00.000Z | 2018-09-01T00:00:00.000Z |

  Scenario: Running an "equalToField" constraint allows one date to be always equal to another with a positive offset
    Given the generator can generate at most 1 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "equalTo",
          "otherField": "foo",
          "offset": 3,
          "offsetUnit": "days"
        }
      """
    Then the following data should be generated:
      | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-04T00:00:00.000Z |

  Scenario: Running an "equalToField" constraint allows one date to be always equal to another with a negative offset
    Given foo is after 2018-01-04T00:00:00.000Z
    And the generator can generate at most 1 rows
    And there is a constraint:
      """
        {
          "field": "foo",
          "is": "equalTo",
          "otherField": "bar",
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
          "is": "equalTo",
          "otherField": "foo",
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
          "is": "equalTo",
          "otherField": "bar",
          "offset": -5,
          "offsetUnit": "working days"
        }
      """
    Then the following data should be generated:
      | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-08T00:00:00.000Z |

