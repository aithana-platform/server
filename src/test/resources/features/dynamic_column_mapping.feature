Feature: Dynamic Column Mapping
  As a user
  I want to provide a YAML file to map CSV columns to tool fields
  So that I can process CSV files with varying structures without manual restructuring


  Background:
    Given the context "src/test/resources/mockData/project.txt"
    And no rate limits
    And an echo custom coder


  Scenario: Successfully process CSV with no custom mapping
    Given the input "src/test/resources/mockData/input/test.csv" in default structure
    And the output "src/test/resources/mockData/output/test.csv"
    When I run the application
    Then no exception is raised

  Scenario: Successfully process CSV with a valid mapping
    Given the custom input "src/test/resources/mockData/input/valid_mapped_test.csv"
    And the output "src/test/resources/mockData/output/valid_mapped_test.csv"
    And the mapper "src/test/resources/mockData/valid_mapped_mapping.yaml"
    When I run the application
    Then no exception is raised

  Scenario: Fails to process CSV with an invalid mapping
    Given the custom input "src/test/resources/mockData/input/invalid_mapped_test.csv"
    And the output "src/test/resources/mockData/output/invalid_mapped_test.csv"
    And the mapper "src/test/resources/mockData/invalid_mapped_mapping.yaml"
    When I run the application
    Then I get a "ColumnMappingException" error
    And the output file remains empty

  #Scenario: Successfully process CSV with valid column mapping by header name
  #Scenario: Fallback to default behavior when no mapping file is provided
  #Scenario: Handle invalid YAML mapping file (syntax error)
  #Scenario: Handle mapping file missing required internal fields
  #Scenario: Ignore unmapped columns in CSV
  #Scenario: Handle CSV missing a column specified in the mapping
  #Scenario: Prioritize column letter over header name if both are present in mapping
  #Scenario: Handle empty CSV file with mapping
  #Scenario: Handle CSV with only headers and mapping