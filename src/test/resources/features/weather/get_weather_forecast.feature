@api
Feature: Get Weather Forecast
In order to get weather forecast information based on required geocode
As a API consumer James
James wants to query different type of weathers forecast 
  Background:
    Given James is at the weather services url
  
    Rule: Get hourly weather forecast

    Scenario Outline: Able to get hourly forecast based on the mandatory geocode only
      When he requests weather forecast hourly based on geocode <geocode>
      Then he should get the weather forecast details
      Examples:
      | geocode                |
      | "1.357896,103.9951533" |
