Feature: Process a Project
  As a researcher using the tool to conduct a thematic analysis
  I want it to treat the input as a project with a context
  So that there's more useful information for the encoding

  Scenario: A single CSV and a project description file
    Given the "src/test/resources/mockData/input/test.csv" CSV file
    And the "src/test/resources/mockData/project.txt" project context file
    When I ask to process
    Then I get no errors
    And I get the results in a file

  Scenario: A single CSV and no project description file
    Given the "src/test/resources/mockData/input/test.csv" CSV file
    When I ask to process
    Then I get "AithanaBuilderException" error
    And no results are written