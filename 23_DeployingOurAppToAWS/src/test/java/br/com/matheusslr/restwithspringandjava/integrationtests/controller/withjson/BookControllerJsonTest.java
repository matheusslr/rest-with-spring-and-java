package br.com.matheusslr.restwithspringandjava.integrationtests.controller.withjson;

import br.com.matheusslr.restwithspringandjava.configs.TestConfigs;
import br.com.matheusslr.restwithspringandjava.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.matheusslr.restwithspringandjava.integrationtests.vo.AccountCredentialsVO;
import br.com.matheusslr.restwithspringandjava.integrationtests.vo.BookVO;
import br.com.matheusslr.restwithspringandjava.integrationtests.vo.TokenVO;
import br.com.matheusslr.restwithspringandjava.integrationtests.vo.wrapper.WrapperBookVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static BookVO bookVO;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        bookVO = new BookVO();
    }

    private void mockBook() {
        bookVO.setTitle("TestTitle");
        bookVO.setAuthor("TestAuthor");
        bookVO.setPrice(24.0);
        bookVO.setLaunchDate(new Date());
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        var accessToken = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class)
                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/books/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    public void testSave() throws IOException, JsonMappingException, JsonProcessingException {
        mockBook();
        var content =
                given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(bookVO)
                        .when()
                        .post()
                        .then()
                        .statusCode(201)
                        .extract()
                        .body()
                        .asString();

        BookVO created = objectMapper.readValue(content, BookVO.class);
        bookVO = created;

        assertNotNull(created.getId());
        assertNotNull(created.getTitle());
        assertNotNull(created.getAuthor());
        assertNotNull(created.getPrice());
        assertNotNull(created.getLaunchDate());

        assertEquals("TestTitle", created.getTitle());
        assertEquals("TestAuthor", created.getAuthor());
        assertEquals(24.0, created.getPrice());

        assertTrue(created.getId() > 0);
    }

    @Test
    @Order(2)
    public void testFindById() throws IOException, JsonMappingException, JsonProcessingException {
        mockBook();

        var content =
                given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .pathParam("id", bookVO.getId())
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        BookVO persistedPerson = objectMapper.readValue(content, BookVO.class);
        bookVO = persistedPerson;

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getTitle());
        assertNotNull(persistedPerson.getAuthor());
        assertNotNull(persistedPerson.getPrice());
        assertNotNull(persistedPerson.getLaunchDate());

        assertEquals("TestTitle", persistedPerson.getTitle());
        assertEquals("TestAuthor", persistedPerson.getAuthor());
        assertEquals(24.0, persistedPerson.getPrice());

        assertTrue(persistedPerson.getId() > 0);
    }

    @Test
    @Order(3)
    public void testUpdate() throws IOException, JsonMappingException, JsonProcessingException {
        bookVO.setAuthor("Aloalo");

        var content =
                given()
                        .spec(specification)
                        .contentType(ContentType.JSON)
                        .body(bookVO)
                        .when()
                        .post()
                        .then()
                        .statusCode(201)
                        .extract()
                        .body()
                        .asString();

        BookVO persistedPerson = objectMapper.readValue(content, BookVO.class);
        bookVO = persistedPerson;

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getTitle());
        assertNotNull(persistedPerson.getAuthor());
        assertNotNull(persistedPerson.getPrice());
        assertNotNull(persistedPerson.getLaunchDate());

        assertEquals("TestTitle", persistedPerson.getTitle());
        assertEquals("Aloalo", persistedPerson.getAuthor());
        assertEquals(24.0, persistedPerson.getPrice());

        assertTrue(persistedPerson.getId() > 0);
    }

    @Test
    @Order(4)
    public void testRemove() throws JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", bookVO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    public void testFindAll() throws IOException, JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperBookVO wrapper = objectMapper.readValue(content, WrapperBookVO.class);
        var people = wrapper.getBookEmbeddedVO().getBooks();

        BookVO foundBookOne = people.get(0);

        assertNotNull(foundBookOne.getId());
        assertNotNull(foundBookOne.getTitle());
        assertNotNull(foundBookOne.getAuthor());
        assertNotNull(foundBookOne.getPrice());
        assertNotNull(foundBookOne.getLaunchDate());

        assertEquals("Abrahan Brewin", foundBookOne.getAuthor());
        assertEquals("Guthry Troucher", foundBookOne.getTitle());
        assertEquals(90.00, foundBookOne.getPrice());

        BookVO foundPersonFour = people.get(3);

        assertNotNull(foundPersonFour.getId());
        assertNotNull(foundPersonFour.getTitle());
        assertNotNull(foundPersonFour.getAuthor());
        assertNotNull(foundPersonFour.getPrice());
        assertNotNull(foundPersonFour.getLaunchDate());

        assertEquals("Ansell Hughes", foundPersonFour.getTitle());
        assertEquals("Adrea Simonetti", foundPersonFour.getAuthor());
        assertEquals(6.00, foundPersonFour.getPrice());
    }

    @Test
    @Order(6)
    public void testHATEOAS() throws IOException, JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/books/v1/896\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/books/v1/375\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/books/v1/1009\"}}}"));

        assertTrue(content.contains("{\"first\":{\"href\":\"http://localhost:8888/api/books/v1?direction=asc&page=0&size=12&sort=author,asc\"}"));
        assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/books/v1?page=0&size=12&direction=asc\"}"));
        assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/books/v1?direction=asc&page=1&size=12&sort=author,asc\"}"));
        assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/books/v1?direction=asc&page=84&size=12&sort=author,asc\"}}"));

        assertTrue(content.contains("\"page\":{\"size\":12,\"totalElements\":1015,\"totalPages\":85,\"number\":0}}"));
    }

    @Test
    @Order(7)
    public void testFindByIdWithoutToken() throws IOException, JsonMappingException, JsonProcessingException {
        mockBook();

        specification = new RequestSpecBuilder()
                .setBasePath("/api/persons/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content =
                given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .pathParam("id", bookVO.getId())
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(403)
                        .extract()
                        .body()
                        .asString();
    }
}
