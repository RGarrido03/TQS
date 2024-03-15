package pt.ua.deti.tqs;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookSearchSteps {
    Library library = new Library();
    List<Book> result = new ArrayList<>();

    @ParameterType("([0-9]{4})-([0-9]{2})-([0-9]{2})")
    public Date iso8601Date(String date) {
        Integer[] split = Arrays.stream(date.split("-")).map(Integer::parseInt).toArray(Integer[]::new);
        return Date.from(LocalDate.of(split[0], split[1], split[1]).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @ParameterType("([0-9]{4})")
    public Date year(String year) {
        return Date.from(LocalDate.of(Integer.parseInt(year), 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Given("I have a list of books")
    public void loadBooks(@NotNull DataTable table) {
        table.cells()
             .stream()
             .skip(1)
             .map(fields -> new Book(fields.getFirst(), fields.get(1), iso8601Date(fields.get(2))))
             .forEach(library::addBook);
    }

    @When("the customer searches for books published between {year} and {year}")
    public void setSearchParameters(final Date from, final Date to) {
        result = library.findBooks(from, to);
    }

    @Then("{int} books should have been found")
    public void verifyAmountOfBooksFound(final int booksFound) {
        assertEquals(result.size(), booksFound);
    }

    @Then("Book {int} should have the title {string}")
    public void verifyBookAtPosition(final int position, final String title) {
        assertEquals(result.get(position - 1).getTitle(), title);
    }
}