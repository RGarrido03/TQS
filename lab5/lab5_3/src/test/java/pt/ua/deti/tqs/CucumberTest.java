package pt.ua.deti.tqs;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("pt/ua/deti/tqs")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "pt.ua.deti.tqs")
public class CucumberTest {
}