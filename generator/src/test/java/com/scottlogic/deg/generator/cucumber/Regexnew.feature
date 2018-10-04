Feature: As a user
        I want to specify that a data field matches a regex
        Because I would like to produce useful test data

  Scenario Outline:
    Given I specify in the profile a field with "<regex-phrase>"
    When I generate the data file
    Then The generated data file should contain "<output-value>"
    Examples:
      | regex-phrase   | output-value |
      | Joh?n\|Mar[yk] | Jon, John, Mary, Mark |
      | FOO: .{2000}   |  FOO: werwr |

