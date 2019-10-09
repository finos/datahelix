Feature: running datetimes related to otherfield datetimes for multiple fields

  Background:
    Given the generation strategy is full
    And there is a field foobar
    And foobar has type "datetime"
    And there is a field foo
    And foo has type "datetime"
    And there is a field bar
    And bar has type "datetime"
    And the combination strategy is exhaustive

  Scenario: Running a "before" and "after" constraint
    Given the generator can generate at most 1 rows
    And foo is anything but null
    And bar is anything but null
    And foobar is anything but null
    And foobar is before field bar
    And foobar is after field foo
    Then the following data should be generated:
      | foobar                   | foo                      | bar                      |
      | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.002Z |

  Scenario: Running a "before" and "equalTo" constraint
    Given the generator can generate at most 1 rows
    And foo is anything but null
    And bar is anything but null
    And foobar is anything but null
    And foobar is before field bar
    And foobar is equal to field foo
    Then the following data should be generated:
      | foobar                   | for                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z |

  Scenario: Running an "after" and "equalTo" constraint
    Given the generator can generate at most 1 rows
    And foo is anything but null
    And bar is anything but null
    And foobar is anything but null
    And foobar is after field bar
    And foobar is equal to field foo
    Then the following data should be generated:
      | foobar                   | for                      | bar                      |
      | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.000Z |

  Scenario: Running two "after" constraints
    Given the generator can generate at most 1 rows
    And foo is anything but null
    And bar is anything but null
    And foobar is anything but null
    And foobar is after field bar
    And foobar is after field foo
    Then the following data should be generated:
      | foobar                   | for                      | bar                      |
      | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.000Z |

  Scenario: Running two "before" constraints switched other field constraints
    Given the generator can generate at most 1 rows
    And foo is anything but null
    And bar is anything but null
    And foobar is anything but null
    And bar is before field foobar
    And foo is before field foobar
    Then the following data should be generated:
      | foobar                   | for                      | bar                      |
      | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.000Z |

  Scenario: Running two "before" constraints
    Given the generator can generate at most 1 rows
    And foo is anything but null
    And bar is anything but null
    And foobar is anything but null
    And foobar is before field bar
    And foobar is before field foo
    Then the following data should be generated:
      | foobar                   | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.001Z |

  Scenario: Running two "after" switched other field constraints
    Given the generator can generate at most 1 rows
    And foo is anything but null
    And bar is anything but null
    And foobar is anything but null
    And bar is after field foobar
    And foo is after field foobar
    Then the following data should be generated:
      | foobar                   | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.001Z |

  Scenario: Running linked "after" constraint
    Given the generator can generate at most 1 rows
    And foo is anything but null
    And bar is anything but null
    And foobar is anything but null
    And foobar is after field bar
    And bar is after field foo
    Then the following data should be generated:
      | foobar                   | for                      | bar                      |
      | 0001-01-01T00:00:00.002Z | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z |

    @ignore # this hangs #1439 has been raised to resolve this
  Scenario: Running linked "before" constraint
    Given the generator can generate at most 1 rows
    And foo is anything but null
    And bar is anything but null
    And foobar is anything but null
    And foobar is before field bar
    And bar is before field foo
    Then the following data should be generated:
      | foobar                   | for                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.002Z | 0001-01-01T00:00:00.001Z |

  Scenario: Running linked "before" constraint with lower limit
    Given the generator can generate at most 1 rows
    And foo is anything but null
    And bar is anything but null
    And foobar is anything but null
    And foobar is before 2019-01-01T00:00:00.000Z
    And foobar is before field bar
    And bar is before field foo
    Then the following data should be generated:
      | foobar                   | for                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.002Z | 0001-01-01T00:00:00.001Z |

  Scenario: Running linked "equalTo" constraint
    Given the generator can generate at most 1 rows
    And foo is anything but null
    And bar is anything but null
    And foobar is anything but null
    And foobar is equal to field bar
    And bar is equal to field foo
    Then the following data should be generated:
      | foobar                   | for                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.000Z |
