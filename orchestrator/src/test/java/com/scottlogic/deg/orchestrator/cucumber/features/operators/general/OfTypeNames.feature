Feature: User can specify that a field must be a type of name

  Background:
    Given the generation strategy is random
    And there is a field foo
    And foo is anything but null

  Scenario: Generating with an of type firstname constraint generates valid firstnames
    Given foo is of type "firstname"
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings longer than or equal to 1

  Scenario: Generating with an of type lastname constraint generates valid lastnames
    Given foo is of type "lastname"
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings longer than or equal to 1

  Scenario: Generating with an of type fullname constraint generates valid fullnames
    Given foo is of type "fullname"
    And the generator can generate at most 5 rows
    Then 5 rows of data are generated
    And foo contains strings matching /.+\s.+/

  Scenario: Generating with type firstname combined with shorter than constraint is successful
    Given foo is of type "firstname"
    And foo is shorter than 5
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings of length between 1 and 4 inclusively

  Scenario: Generating with type lastname combined with shorter than constraint is successful
    Given foo is of type "lastname"
    And foo is shorter than 5
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings of length between 1 and 4 inclusively

  Scenario: Generating with type fullname combined with shorter than constraint is successful
    Given foo is of type "fullname"
    And foo is shorter than 10
    And the generator can generate at most 5 rows
    Then 5 rows of data are generated
    And foo contains strings of length between 1 and 9 inclusively
    And foo contains strings matching /.+\s.+/

  Scenario: Generating with type firstname combined with longer than constraint is successful
    Given foo is of type "firstname"
    And foo is longer than 5
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings longer than or equal to 6

  Scenario: Generating with type lastname combined with longer than constraint is successful
    Given foo is of type "lastname"
    And foo is longer than 5
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings longer than or equal to 6

  Scenario: Generating with type fullname combined with longer than constraint is successful
    Given foo is of type "fullname"
    And foo is longer than 10
    And the generator can generate at most 5 rows
    Then 5 rows of data are generated
    And foo contains strings longer than or equal to 11
    And foo contains strings matching /.+\s.+/

  Scenario: Generating with type firstname combined with of length constraint is successful
    Given foo is of type "firstname"
    And foo is of length 5
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings of length between 5 and 5 inclusively

  Scenario: Generating with type lastname combined with of length constraint is successful
    Given foo is of type "lastname"
    And foo is of length 5
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings of length between 5 and 5 inclusively

  Scenario: Generating with type fullname combined with of length constraint is successful
    Given foo is of type "fullname"
    And foo is of length 10
    And the generator can generate at most 5 rows
    Then 5 rows of data are generated
    And foo contains strings of length between 10 and 10 inclusively
    And foo contains strings matching /.+\s.+/

  Scenario: Generating with type firstname combined with matching RegEx constraint is successful
    Given foo is of type "firstname"
    And foo is matching regex /[A]{1}.{2,4}/
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings of length between 3 and 5 inclusively
    And foo contains strings matching /[A]{1}.{2,4}/

  Scenario: Generating with type lastname combined with matching RegEx constraint is successful
    Given foo is of type "lastname"
    And foo is matching regex /[A]{1}.{2,4}/
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings of length between 3 and 5 inclusively
    And foo contains strings matching /[Aa]{1}.{2,4}/

  Scenario: Generating with type fullname combined with matching RegEx constraint is successful
    Given foo is of type "fullname"
    And foo is matching regex /[A]{1}.{5,10}/
    And the generator can generate at most 5 rows
    Then 5 rows of data are generated
    And foo contains strings of length between 6 and 11 inclusively
    And foo contains strings matching /[A]{1}.{5,10}/
    And foo contains strings matching /.+\s.+/

  Scenario: Generating with type firstname combined with containing RegEx constraint is successful
    Given foo is of type "firstname"
    And foo is containing regex /[Aa]{1}.{1}/
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings longer than or equal to 2
    And foo contains strings matching /.*[Aa]{1}.{1}.*/

  Scenario: Generating with type lastname combined with containing RegEx constraint is successful
    Given foo is of type "lastname"
    And foo is containing regex /[Aa]{1}N{1}/
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings longer than or equal to 2
    And foo contains strings matching /.*[Aa]{1}.{1}.*/

  Scenario: Generating with type fullname combined with containing RegEx constraint is successful
    Given foo is of type "fullname"
    And foo is containing regex /[Aa]{1}N{1}/
    And the generator can generate at most 5 rows
    Then 5 rows of data are generated
    And foo contains strings matching /.+\s.+/
    And foo contains strings matching /.*[Aa]{1}.{1}.*/
