@api
Feature: DEMO_RESTAPI


  Background:
    Given James is at the base url

    @test
  Scenario Outline: create a new message (<hiptest-uid>)
    In order to create a new message using POST
    As a API developer James
    James wants to create a new message
    When James want to create a new message with author "<author>" and message "<message>"
    Then he is able to create the new message

    Examples:
      | author | message | hiptest-uid |
      | J.R.R. Tolkien | The Lord of the Rings | uid b5923c4e-2402-4f26-8fa0-4d559821cd6d |

      @test
  Scenario: get a single message (uid 4a386f39-a333-4b30-81da-ae1be974db69)
    In order to get a single message
    As a API developer James
    James wants to get a single message
    When James want to get a single message
    Then he is able to get a single message


    @test
  Scenario: get all messages (uid d76caa0b-915e-4fd6-8c05-8199596fe8de)
    In order to view all messages 
    As a API developer James
    James wants to get all messages to be displayed
    When James view all messages
    Then he is able to view all the messages

    @wip
  Scenario: get all messages with given author (uid 68e7597a-25f7-487a-ab54-8157c3332698)
    In order to view all messages with a given author
    As a API developer James
    James wants to view all messages with the query parameter author
    When James want to view all messages with the query parameter author
    Then he is able to view all messages with the parameter author

    @test
  Scenario Outline: update a message (<hiptest-uid>)
    In order to update a message
    As a API developer James
    James wants to update a message
    When James update a message with author "<author>" and "<message>"
    Then the message get updated

    Examples:
      | author | message | hiptest-uid |
      | john smart ferguson |  | uid 06275112-eaa3-4834-aa97-60fd66b98c7c |

      @test
  Scenario Outline: delete a message (<hiptest-uid>)
    In order to delete a message
    As a API developer James
    James wants to able to delete a message
    When James delete a message "<message>"
    Then he is able to delete the message

    Examples:
      | message | hiptest-uid |
      | 1 | uid 1e50b592-02d1-4e76-bcae-6dfb27b8c230 |
