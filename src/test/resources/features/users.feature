Feature: Users tests

  Scenario: Retrieve all users is OK
    When I retrieve all users
    Then I get http status users 200

  Scenario: Retrieve user by id is OK
    When I retrieve user by id 1
    Then I get http status users 200