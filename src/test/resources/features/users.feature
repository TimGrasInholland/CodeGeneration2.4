Feature: Users tests

  Scenario: Retrieve all users is OK
    When I retrieve all users
    Then I get http status users 200

  Scenario: Retrieve user by id is OK
    When I retrieve user by id 1
    Then I get http status users 200

  Scenario: Create an User is CREATED
    When I create an user
    Then I get http status users 201

  Scenario: Retrieve an user by username
    When I retrieve an user by username 'Adrie538'
    Then I get http status users 200

  Scenario: Retrieve an user by lastname
    When I retrieve an user by lastname 'Komen'
    Then I get http status users 200