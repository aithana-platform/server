Feature: Process a Project
  As a researcher using the tool to conduct a thematic analysis
  I want it to treat the input as a project with a context
  So that there's more useful information for the encoding

  Scenario: A single CSV and a project description file via CLI
    Given --input="src/test/resources/mockData/input/test.csv"
    And an output
    And --context="src/test/resources/mockData/project.txt"
    When I ask to process
    Then I get no errors
    And I get the results in a file

  Scenario: A single CSV and no project description file via CLI
    Given --input="src/test/resources/mockData/input/test.csv"
    And an output
    When I ask to process
    Then I get an error
    And no results are written