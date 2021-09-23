
Feature: Create farmer
  In order to create a new farmer profile
  As a API farmer services consumer James
  James wants to create a new farmer profile
  Background:
    Given James is at the farmer services url

  Rule: Create farmer using yara UserID
    @test
    Scenario: Able to create farmer using unique yara UserID
      When he create a farmer using a unique yara UserID
      Then he should able to get the details of the new farmer profile
