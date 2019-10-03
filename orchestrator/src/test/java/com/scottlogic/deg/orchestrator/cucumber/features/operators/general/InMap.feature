Feature: User can specify that a field value belongs to a set of predetermined options.

  Background:
    Given the generation strategy is full

### inMap alone ###
  Scenario: Running an 'inMap'
    Given the following fields exist:
      |Home Nation |
      |Capital     |
    And the file "testFile" contains the following data:
      |Country           |Capital    |
      |England           |London     |
      |Northern Ireland  |Belfast    |
      |Scotland          |Edinburgh  |
      |Wales             |Cardiff    |
    And Home Nation has type "string"
    And Capital has type "string"
    And Home Nation is from Country in testFile
    And Capital is from Capital in testFile
    Then the following data should be generated:
      |Home Nation         |Capital      |
      |"England"           |"London"     |
      |"Northern Ireland"  |"Belfast"    |
      |"Scotland"          |"Edinburgh"  |
      |"Wales"             |"Cardiff"    |

  Scenario: Running an 'inMap' with text a restriction
    Given the following fields exist:
      |Home Nation |
      |Capital     |
    And the file "testFile" contains the following data:
      |Country           |Capital    |
      |England           |London     |
      |Northern Ireland  |Belfast    |
      |Scotland          |Edinburgh  |
      |Wales             |Cardiff    |
    And Home Nation has type "string"
    And Capital has type "string"
    And Home Nation is from Country in testFile
    And Capital is from Capital in testFile
    When Capital is matching regex /^[BE].*/
    When Home Nation is matching regex /^[S].*/
    Then the following data should be generated:
      |Home Nation         |Capital      |
      |"Scotland"          |"Edinburgh"  |
