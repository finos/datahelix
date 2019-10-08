Feature: User can specify that a field must be a type of name

  Background:
    Given the generation strategy is random
    And there is a field foo
    And foo has type "string"
    And foo is anything but null

  Scenario: Generating with an of type firstname constraint generates valid firstnames
    Given foo has type "firstname"
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings longer than or equal to 1

  Scenario: Generating with an of type lastname constraint generates valid lastnames
    Given foo has type "lastname"
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings longer than or equal to 1

  Scenario: Generating with an of type fullname constraint generates valid fullnames
    Given foo has type "fullname"
    And the generator can generate at most 5 rows
    Then 5 rows of data are generated
    And foo contains strings matching /.+\s.+/
