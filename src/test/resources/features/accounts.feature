Feature: Accounts tests

  Scenario: Retrieve all accounts is OK
    When I retrieve all accounts
    Then I get http status account 200

  Scenario: Retrieve all accounts with limit and offset is OK
    When I retrieve all accounts with limit 2 and offset 1
    Then I get http status account 200

  Scenario: Create an account is CREATED
    When I create an account
    Then I get http status account 201

  Scenario: Disable an account is OK
    When I disable an account
    Then I get http status account 200

  Scenario: Retrieve an account by IBAN is OK
    When I retrieve an account by iban "NL01INHO4995677694"
    Then I get http status account 200

  Scenario: Retrieve an account by UserId is Ok
    When I retrieve an account user id 2
    Then I get http status account 200
