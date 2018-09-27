Feature: User can specify that a value either matches or contains a specified regex

  Scenario:
    Given there is a field foo
    And foo is matchingRegex /a{1,3}/
    Then the following data should be generated:
      | foo |
      | a   |
      | aa  |
      | aaa |

  Scenario: A field is constrained with /^pattern/
    Given the following fields exist:
      | foo |
      | foo |

    And foo is matchingRegex /^foo/
    Then expect:
      | foo |


    Given there is a field title
    And title is matchingRegex /^M/
    Then there must be at least 500 values
    And they should all start with M

  Scenario: A field is constrained with /pattern$/
    Given the following fields exist:
      | foo | bar  foo |
    And foo is matchingRegex /foo$/
    Then expect:
      | foo | bar foo |

  Scenario: A field is constrained with /[0-4}/
    Given there is a field foo
    And foo is matchingRegex /[0-4]/
    Then expect exactly:
    | foo |
    | 0 |
    | 1 |
    | 2 |
    | 3 |
    | 4 |

  Scenario: A field is constrained with /.*/
    Given there is a field foo
    And foo is matchingRegex /.*/
    Then there must be at least 500 values
    And they should include ""


  Scenario: A field is constrained with /.+/
    Given there is a field foo
    And foo is matchingRegex /.+/
    Then there must be at least 500 values
    And they should not include ""


  Scenario:
    Given there is a field foo
    And foo is matchingRegex /a{2}/
    Then the following data should be generated:
      | aa  |