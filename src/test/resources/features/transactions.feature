Feature: Transactions tests

  Scenario: Create a transaction is CREATED
    When I create a transaction
    Then I get http status transaction 201

  Scenario: Retrieve all transactions is OK
    When I retrieve all transactions
    Then I get http status transaction 200

  Scenario: Retrieve all transactions with limit, offset and username is OK
    When I retrieve all transactions with limit 3, offset 1 and username "Adrie538"
    Then I get http status transaction 200

  Scenario: Retrieve all transactions with limit, offset and dateFrom and dateTo is OK
    When I retrieve all transactions with limit 2, offset 1 and dateFrom "2020-05-16" and dateTo "2020-05-23"
    Then I get http status transaction 200

  Scenario: Retrieve an transaction by AccountId is OK
    When I retrieve transactions by accountId 2
    Then I get http status transaction 200

  Scenario: Retrieve transactions by UserId is Ok
    When I retrieve transactions by userId 2
    Then I get http status transaction 200