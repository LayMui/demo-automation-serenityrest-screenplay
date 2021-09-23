#!/bin/bash

display_usage() {
  echo "This script must be run with at least 1 arguments"
 	echo -e "Usage:" $0 "[sync|update_features|run_tests|push_results] [local|dev|staging|prod]"
	}

if [  $# -lt 1 ]
then
		display_usage
		exit 1
fi

TASK=$1
ENV=$2

case $ENV in
    dev)
      ENV_PARAMETER_REST="-Drestapi.baseurl=http://localhost:8080/webapp"
      ;;
    prod)
      ENV_PARAMETER_REST="-Drestapi.baseurl=http://localhost:8080/webapp"
      ;;
    *)
      ENV_PARAMETER_REST="-Drestapi.baseurl=http://localhost:8080/webapp"
      ;;
esac

set_variables() {

  if [ "$API_UID" = "" ] ||
  [ "$API_ACCESS_TOKEN" = "" ] ||
  [ "$API_CLIENT" = "" ] ||
  [ "$PROJECT_TOKEN" = "" ] ||
  [ "$PROJECT_ID" = "" ] ||
  [ "$TEST_RUN_ID" = "" ]; then
    echo "Set the following Environment Variables"
    echo "API_UID, API_ACCESS_TOKEN, API_CLIENT"
    echo "PROJECT_TOKEN, PROJECT_ID, TEST_RUN_ID"
  fi
}

synchronise() {
  if [ -f project_scenarios.json ]; then
    rm -Rf project_scenarios.json
  fi

  if [ -f testrun_scenarios.json ]; then
    rm -Rf testrun_scenarios.json
  fi

 echo "Synchronize a test run"
 curl -sS -XPOST "https://studio.cucumber.io/api/projects/$PROJECT_ID/test_runs/$TEST_RUN_ID/synchronize" \
  -H "accept: application/vnd.api+json; version=1" \
  -H "access-token: $API_ACCESS_TOKEN" \
  -H "uid: $API_UID" \
  -H "client: $API_CLIENT"

 echo "Getting all scenarios from a project"
 response=$(curl -sS "https://studio.cucumber.io/api/projects/$PROJECT_ID/scenarios" \
  -H "accept: application/vnd.api+json; version=1" \
  -H "access-token: $API_ACCESS_TOKEN" \
  -H "uid: $API_UID" \
  -H "client: $API_CLIENT" \
  -o project_scenarios.json \
  -w %{http_code})

 if [[ $? -eq 0 && $response =~ ^2 ]]; then
  #readarray -t project_arr < <(jq -r '.data[].id' project_scenarios.json)
  project_arr=($(jq -r '.data[].id' project_scenarios.json))
  echo "Getting all scenarios from a test run"
  response=$(curl -sS "https://studio.cucumber.io/api/projects/$PROJECT_ID/test_runs/$TEST_RUN_ID/test_snapshots?include=scenario" \
    -H "accept: application/vnd.api+json; version=1" \
    -H "access-token: $API_ACCESS_TOKEN" \
    -H "uid: $API_UID" \
    -H "client: $API_CLIENT" \
    -o testrun_scenarios.json \
    -w %{http_code})

  if [[ $? -eq 0 && $response =~ ^2 ]]; then
  # readarray -t testrun_arr < <(jq -r '.data[].relationships.scenario.data.id' testrun_scenarios.json)
   testrun_arr=($(jq -r '.data[].relationships.scenario.data.id' testrun_scenarios.json))

    echo "Comparing collections..."
    for scenarioId in "${project_arr[@]}"
    do
      if [[ ! " ${testrun_arr[@]} " =~ " ${scenarioId} " ]]; then
        echo "Adding missing scenarios to existing test run"
        curl -sS -XPATCH https://studio.cucumber.io/api/projects/$PROJECT_ID/test_runs/$TEST_RUN_ID \
           -H "accept: application/vnd.api+json; version=1" \
           -H "access-token: $API_ACCESS_TOKEN" \
           -H "uid: $API_UID" \
           -H "client: $API_CLIENT" \
           --data '{"data": {"type": "test_runs", "id": '"$TEST_RUN_ID"', "attributes": {"scenario_id": '"$scenarioId"'}}}'
        echo ""
        sleep 5
      fi
    done
    echo "Comparison complete"
  else
    echo "ERROR: Failed to retrieve scenarios from testrun, response code received was $response"
  fi
else
  echo "ERROR: Failed to retrieve scenarios from project, response code received was $response"
fi

 rm -Rf project_scenarios.json
 rm -Rf testrun_scenarios.json
}

set_variables

case $TASK in
   sync)
     synchronise
     ;;
   update_features)
          echo "Update Feature files from Cucumber Studio with test run id $TEST_RUN_ID"
          rm -fr src/test/resources/features/*
          docker run --rm \
          -v ${PWD}:/app hiptest/hiptest-publisher \
          --config-file hiptest-publisher.conf --token=$PROJECT_TOKEN --test-run-id $TEST_RUN_ID --only=features
      ;;
    run_tests)
       cp webapp.war ~/dev/apache-tomcat-9.0.48/webapps
        mvn -U -DskipTests=true clean install
        mvn -U clean verify -Dcucumber.filter.tags=@test -Dserenity.project.name="Serenity-Rest" $ENV_PARAMETER_REST serenity:reports -Dserenity.reports=single-page-html
        ;;
    push_results)
      echo "Push Test Result back to Cucumber Studio"
      docker run --rm \
        -v ${PWD}:/app hiptest/hiptest-publisher \
        hiptest-publisher --config-file hiptest-publisher.conf --token=$PROJECT_TOKEN --push="target/site/serenity/SERENITY-JUNIT-*.xml" --test-run-id $TEST_RUN_ID --push-format=junit
      ;;
    *)
      display_usage
      exit 1
      ;;
esac




