package pt.ua.deti.tqs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

public class CalculatorSteps {

    static final Logger log = getLogger(lookup().lookupClass());

    private Calculator calc;

    @Given("a calculator I just turned on")
    public void setup() {
        calc = new Calculator();
    }

    @When("I add {int} and {int}")
    public void add(int arg1, int arg2) {
        log.debug("Adding {} and {}", arg1, arg2);
        calc.push(arg1);
        calc.push(arg2);
        calc.push("+");
    }

    @When("I substract {int} to {int}")
    public void substract(int arg1, int arg2) {
        log.debug("Substracting {} and {}", arg1, arg2);
        calc.push(arg1);
        calc.push(arg2);
        calc.push("-");
    }

    @When("I multiply {int} by {int}")
    public void multiply(int arg0, int arg1) {
        log.debug("Multiplying {} by {}", arg0, arg1);
        calc.push(arg0);
        calc.push(arg1);
        calc.push("*");
    }

    @When("I divide {int} by {int}")
    public void divide(int arg0, int arg1) {
        log.debug("Dividing {} by {}", arg0, arg1);
        calc.push(arg0);
        calc.push(arg1);
        calc.push("/");
    }

    @Then("the result is {double}")
    public void the_result_is(double expected) {
        Number value = calc.value();
        log.debug("Result: {} (expected {})", value, expected);
        assertEquals(expected, value);
    }

    @Then("the result is infinity")
    public void theResultIsInfinity() {
        the_result_is(Double.POSITIVE_INFINITY);
    }
}