package pt.ua.deti.tqs.lab7_3;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create")
class FlywayIT {
    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer("postgres:16")
            .withUsername("user")
            .withPassword("password")
            .withDatabaseName("test");

    @LocalServerPort
    int localPortForTestServer;

    @Autowired
    private BookRepository repository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    void whenValidInput_thenCreateBook() {
        Book book = new Book();
        book.setAuthor("Suzanne Collins");
        book.setTitle("The Hunger Games");
        book.setYear(2008);

        String endpoint = UriComponentsBuilder.newInstance()
                                              .scheme("http")
                                              .host("localhost")
                                              .port(localPortForTestServer)
                                              .pathSegment("api", "book")
                                              .build()
                                              .toUriString();


        RestAssured.given().contentType("application/json").body(book)
                   .post(endpoint)
                   .then().statusCode(HttpStatus.CREATED.value()).contentType("application/json")
                   .body("author", is(book.getAuthor()))
                   .body("title", is(book.getTitle()))
                   .body("year", is(book.getYear()));
    }

    @Test
    void givenEmployees_whenGetEmployees_thenStatus200() {
        createTestBook("Suzanne Collins", "The Hunger Games: Catching Fire", 2009);
        createTestBook("Suzanne Collins", "The Hunger Games: Mockingjay", 2010);

        String endpoint = UriComponentsBuilder.newInstance()
                                              .scheme("http")
                                              .host("localhost")
                                              .port(localPortForTestServer)
                                              .pathSegment("api", "book")
                                              .build()
                                              .toUriString();

        RestAssured.given()
                   .get(endpoint)
                   .then().statusCode(HttpStatus.OK.value())
                   .body("title", hasItems("The Hunger Games: Catching Fire", "The Hunger Games: Mockingjay"));
    }

    private void createTestBook(String author, String title, Integer year) {
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(title);
        book.setYear(year);

        repository.saveAndFlush(book);
    }
}

