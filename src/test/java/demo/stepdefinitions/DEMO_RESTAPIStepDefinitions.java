package demo.stepdefinitions;

import demo.dto.MessageDto;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.interactions.Put;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.util.EnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.CoreMatchers.hasItems;

public class DEMO_RESTAPIStepDefinitions {

    private String theRestApiBaseUrl;
    EnvironmentVariables environmentVariables;
    private final Logger log = LoggerFactory.getLogger(DEMO_RESTAPIStepDefinitions.class);
    Actor james;


    @Before(value = "@api")
    public void setTheRestApiBaseUrl() {
        theRestApiBaseUrl =
                environmentVariables
                        .optionalProperty("restapi.baseurl")
                        .orElse("http://localhost:8080/webapp");

        log.info("theRestApiBaseUrl: " + theRestApiBaseUrl);
        james = Actor.named("James the mobile user").whoCan(CallAnApi.at(theRestApiBaseUrl));
    }

    @After("@api")
    public void tearDown() {

    }

    @Given("^(?:.*) is at the base url")
    public void jamesIsAtTheBaseUrl() {
        // Already at the base url
    }

    @When("^(?:.*) update a message with author \"(.*)\" and \"(.*)\"")
    public void jamesUpdateAMessageWithAuthorAnd(String author, String message) {

        MessageDto messageDto = new MessageDto(author, message);

        james.attemptsTo(Put.to("/taqelah/messages/2").
                with(request -> {
                    request.body(messageDto);
                    request.header("Content-Type", "application/json");
                    return request;
                }));

    }
    @Then("^the message get updated$")
    public void theMessageGetUpdated() {
        james.should(
                seeThatResponse(
                        "the expected response should be returned", response -> response.statusCode(200)));
        String author = SerenityRest.then().extract().body().jsonPath().get("author");
        String message =
                SerenityRest.then().extract().body().jsonPath().get("message");
        log.info("author:" + author);
        log.info("message:" + message);
        Ensure.that("author is returned", response -> author.equals(author))
                .andThat(
                        "message is returned",
                        response -> message.equals(message));

    }

    @When("^(?:.*) want to get a single message$")
    public void jamesWantToGetASingleMessage() {

        james.attemptsTo(Get.resource("/taqelah/messages/2")
                .with(request -> {
                            return request.relaxedHTTPSValidation();
                }));

    }

    @Then("^he is able to get a single message$")
    public void heIsAbleToGetASingleMessage() {
        james.should(
                seeThatResponse(
                        "the expected response should be returned", response -> response.statusCode(200)));

        String author = SerenityRest.then().extract().body().jsonPath().get("author");
        String message =
                SerenityRest.then().extract().body().jsonPath().get("message");
        log.info("author:" + author);
        log.info("message:" + message);
        Ensure.that("author is returned", response -> author.equals("Author2"))
                .andThat(
                        "message is returned",
                        response -> message.equals("Message2"));
    }

    @When("^(?:.*) view all messages$")
    public void jamesViewAllMessages() {
        james.attemptsTo(Get.resource("/taqelah/messages")
                .with(request -> {
                    return request.relaxedHTTPSValidation();
                }));
    }

    @Then("^he is able to view all the messages$")
    public void heIsAbleToViewAllTheMessages() {
        String jsonArrayString = SerenityRest.then().extract().body().jsonPath().prettyPrint();
        log.info("ARRAY: "  + jsonArrayString);
        james.should(
                seeThatResponse(
                        "the expected response should be returned", response -> response.statusCode(200)
                                .body("author", hasItems("Author1", "Author2", "J.R.R. Tolkien"))
                                .body("message", hasItems("Message1", "Message2", "The Lord of the Rings"))));

    }

    @When("^(?:.*) delete a message \"(.*)\"")
    public void james_delete_a_message(String message) {
       james.attemptsTo(
            Delete.from("/taqelah/messages/{message}")
                    .with(request -> {
                        request.relaxedHTTPSValidation();
                        request.pathParam("message", message);
                        return request.header("Content-Type", "application/json");
                    }));

    }

    @Then("^he is able to delete the message$")
    public void he_is_able_to_delete_the_message() {
        james.should(
                seeThatResponse(
                        "the expected response should be returned", response -> response.statusCode(200)));

    }

    @When("^(?:.*) want to create a new message with author \"(.*)\" and message \"(.*)\"")
    public void jamesWantToCreateANewMessageWithAuthorAndMessage(String author, String message) {
        MessageDto messageDto = new MessageDto(author, message);
        james.attemptsTo(
            Post.to("/taqelah/messages/")
                .with(request -> {
                    request.relaxedHTTPSValidation();
                    request.body(messageDto);
                    request.header("Content-Type", "application/json");
                    return request;
                }));
    }

    @Then("^he is able to create the new message$")
    public void heIsAbleToCreateTheNewMessage() {
        james.should(
                seeThatResponse(
                        "the expected response should be returned", response -> response.statusCode(201)));

        String author = SerenityRest.then().extract().body().jsonPath().get("author");
        String message =
                SerenityRest.then().extract().body().jsonPath().get("message");
        log.info("author:" + author);
        log.info("message:" + message);
        Ensure.that("author is returned", response -> author.equals(author))
                .andThat(
                        "message is returned",
                        response -> message.equals(message));
    }
}
