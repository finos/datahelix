package com.scottlogic.deg.generator.cucumber;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegexStep {

    @Given("^I specify in the profile a field with \"([^\"]*)\"$")
    public void i_specify_in_the_profile_a_field_with_a(String regex) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I generate the data file$")
    public void i_generate_the_data_file() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^The generated data file should contain \"([^\"]*)\"$")
    public void the_generated_data_file_should_contain_a_aa_aaa(String outputValue) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }


    @And("^it should contain no more than (\\d) characters$")
    public void itShouldContainNoMoreThanNumberOfCharsCharacters() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    // \"([^\"]*)\"

}

