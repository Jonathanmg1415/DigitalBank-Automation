package co.edu.udea.calidad.digitalbank.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

/**
 * Ejecuta todos los features. Para correr solo algunos escenarios:
 * {@code gradlew clean test aggregate -Dcucumber.filter.tags="@HU-11"}.
 */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "co.edu.udea.calidad.digitalbank.stepdefinitions",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class Runner {}
