Feature: The violations mode of the Data Helix app can be run in violations mode to create data

Background:
     Given the generation strategy is full
       And there is a field foo
       And the data requested is violating
       And the generator can generate at most 5 rows

Scenario: Running the generator in violate mode for not equal to is successful
     Given foo is anything but equal to 8
     Then the following data should be generated:
       | foo  |
       | 8    |
       | null |

Scenario: Running the generator in violate mode where equal to is not violated is successful
     Given foo is equal to 8
       And we do not violate any equal to constraints
     Then the following data should be generated:
       | foo  |
       | 8    |
       | null |