# Exercise 1
## a) Identify a couple of examples that use AssertJ expressive methods chaining.
```java
@Test
void givenSetOfEmployees_whenFindAll_thenReturnAllEmployees() {
    Employee alex = new Employee("alex", "alex@deti.com");
    Employee ron = new Employee("ron", "ron@deti.com");
    Employee bob = new Employee("bob", "bob@deti.com");

    entityManager.persist(alex);
    entityManager.persist(bob);
    entityManager.persist(ron);
    entityManager.flush();

    List<Employee> allEmployees = employeeRepository.findAll();

    assertThat(allEmployees).hasSize(3).extracting(Employee::getName).containsOnly(alex.getName(), ron.getName(), bob.getName());
}
```

```java
@Test
void whenValidInput_thenCreateEmployee() {
    Employee bob = new Employee("bob", "bob@deti.com");
    mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(bob)));

    List<Employee> found = repository.findAll();
    assertThat(found).extracting(Employee::getName).containsOnly("bob");
}
```

## b) Identify an example in which you mock the behavior of the repository (and avoid involving a database).
On B tests (*business logic*), the `Mockito.when().thenReturn()` calls mock the repository.

```java
@BeforeEach
public void setUp() {
    // These expectations provide an alternative to the use of the repository
    Employee john = new Employee("john", "john@deti.com");
    john.setId(111L);

    Employee bob = new Employee("bob", "bob@deti.com");
    Employee alex = new Employee("alex", "alex@deti.com");

    List<Employee> allEmployees = Arrays.asList(john, bob, alex);

    Mockito.when(employeeRepository.findByName(john.getName())).thenReturn(john);
    Mockito.when(employeeRepository.findByName(alex.getName())).thenReturn(alex);
    Mockito.when(employeeRepository.findByName("wrong_name")).thenReturn(null);
    Mockito.when(employeeRepository.findById(john.getId())).thenReturn(Optional.of(john));
    Mockito.when(employeeRepository.findAll()).thenReturn(allEmployees);
    Mockito.when(employeeRepository.findById(-99L)).thenReturn(Optional.empty());
}
```

## c) What is the difference between standard `@Mock` and `@MockBean`?
`@Mock` is given by Mockito, whereas `@MockBean` is made by Spring Boot itself.
Since `@Mock` doesn't know anything about Spring beans (and vice-versa), a bean mock must be used in order to bypass a real bean.

## d) What is the role of the file `application-integrationtest.properties`? In which conditions will it be used?
This file contains Spring application properties that will be used in integration tests.
On unit tests, a mock is used (since the goal is to test the logic), but on integration tests real connections are needed.
That way, this file injects a real-world database scenario.

## e) The sample project demonstrates three test strategies to assess an API (C, D and E) developed with SpringBoot. Which are the main/key differences?
C mocks the Service and so it doesn't perform any database query, since answers are mocked.

Both D and E contain integration tests and thus use a real database connection.
D uses `MockMvc`, which mocks the Model-View-Controller layout, whereas E uses a `RestTemplate`, which acts as a REST API crawler.
The first one parses the response directly, while the second one gets the response, and then uses AssertJ asserts on it.
