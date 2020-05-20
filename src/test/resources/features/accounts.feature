Feature: Accounts tests

  Scenario: Retrieve all accounts is OK
    When I retrieve all accounts
    Then I get http status 200