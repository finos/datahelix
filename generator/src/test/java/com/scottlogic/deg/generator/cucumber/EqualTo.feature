Feature: User can specify that a value is equalTo a required value

  Scenario:
    Given there is a field foo
    And foo is equal to "String24£"
    And foo is of type "string"
    Then the following data should be generated:
      | foo |
      |String24£|

  Scenario:
    Given there is a field foo
    When foo is equal to 23
    And foo is of type "numeric"
    Then the following data should be generated:
      | foo |
      |23|


#  Scenario:
#    Given there is a field foo
#    And foo is equal to 2018-01-03T00:00:00.000
#    And foo is not null
#    And foo is of type "temporal"
#    And foo is formatted as "%tF"
#    Then the following data should be generated:
#      | foo |
#      | 2018-01-03T00:00:00.000 |


  Scenario: User requires to create a numeric field with data values that are greater than zero
    Given there is a field foo
    And foo is greater than 0
    And foo is less than 2
    And foo is granular to 1
    And foo is equal to 1
    And foo is not null
    Then the following data should be generated:
      | foo |
      | 1   |