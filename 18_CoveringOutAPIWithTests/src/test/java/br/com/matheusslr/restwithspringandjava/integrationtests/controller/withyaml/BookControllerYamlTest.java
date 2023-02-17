package br.com.matheusslr.restwithspringandjava.integrationtests.controller.withyaml;

import br.com.matheusslr.restwithspringandjava.configs.TestConfigs;
import br.com.matheusslr.restwithspringandjava.integrationtests.controller.withyaml.mapper.YamlMapper;
import br.com.matheusslr.restwithspringandjava.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.matheusslr.restwithspringandjava.integrationtests.vo.AccountCredentialsVO;
import br.com.matheusslr.restwithspringandjava.integrationtests.vo.BookVO;
import br.com.matheusslr.restwithspringandjava.vo.v1.security.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YamlMapper objectMapper;

    private static BookVO bookVO;

    @BeforeAll
    public static void setup() {
        objectMapper = new YamlMapper();
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
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfigs.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .body(user, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class, objectMapper)
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
        var created =
                given()
                        .config(
                                RestAssuredConfig
                                        .config()
                                        .encoderConfig(EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(
                                                        TestConfigs.CONTENT_TYPE_YML,
                                                        ContentType.TEXT)))
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .accept(TestConfigs.CONTENT_TYPE_YML)
                        .body(bookVO, objectMapper)
                        .when()
                        .post()
                        .then()
                        .statusCode(201)
                        .extract()
                        .body()
                        .as(BookVO.class, objectMapper);

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

        var persistedBook =
                given()
                        .config(
                                RestAssuredConfig
                                        .config()
                                        .encoderConfig(EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(
                                                        TestConfigs.CONTENT_TYPE_YML,
                                                        ContentType.TEXT)))
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .accept(TestConfigs.CONTENT_TYPE_YML)
                        .pathParam("id", bookVO.getId())
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(BookVO.class, objectMapper);

        bookVO = persistedBook;

        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getLaunchDate());

        assertEquals("TestTitle", persistedBook.getTitle());
        assertEquals("TestAuthor", persistedBook.getAuthor());
        assertEquals(24.0, persistedBook.getPrice());

        assertTrue(persistedBook.getId() > 0);
    }

    @Test
    @Order(3)
    public void testUpdate() throws IOException, JsonMappingException, JsonProcessingException {
        bookVO.setAuthor("Aloalo");

        var persistedBook =
                given()
                        .spec(specification)
                        .config(
                                RestAssuredConfig
                                        .config()
                                        .encoderConfig(EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(
                                                        TestConfigs.CONTENT_TYPE_YML,
                                                        ContentType.TEXT)))
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .accept(TestConfigs.CONTENT_TYPE_YML)
                        .body(bookVO, objectMapper)
                        .when()
                        .post()
                        .then()
                        .statusCode(201)
                        .extract()
                        .body()
                        .as(BookVO.class, objectMapper);

        bookVO = persistedBook;

        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getLaunchDate());

        assertEquals("TestTitle", persistedBook.getTitle());
        assertEquals("Aloalo", persistedBook.getAuthor());
        assertEquals(24.0, persistedBook.getPrice());

        assertTrue(persistedBook.getId() > 0);
    }

    @Test
    @Order(4)
    public void testRemove() throws JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
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
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfigs.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookVO[].class, objectMapper);

        List<BookVO> people = List.of(content);

        BookVO foundBookOne = people.get(0);

        assertNotNull(foundBookOne.getId());
        assertNotNull(foundBookOne.getTitle());
        assertNotNull(foundBookOne.getAuthor());
        assertNotNull(foundBookOne.getPrice());
        assertNotNull(foundBookOne.getLaunchDate());

        assertEquals("Michael C. Feathers", foundBookOne.getAuthor());
        assertEquals("Working effectively with legacy code", foundBookOne.getTitle());
        assertEquals(49.00, foundBookOne.getPrice());

        BookVO foundPersonFour = people.get(3);

        assertNotNull(foundPersonFour.getId());
        assertNotNull(foundPersonFour.getTitle());
        assertNotNull(foundPersonFour.getAuthor());
        assertNotNull(foundPersonFour.getPrice());
        assertNotNull(foundPersonFour.getLaunchDate());

        assertEquals("JavaScript", foundPersonFour.getTitle());
        assertEquals("Crockford", foundPersonFour.getAuthor());
        assertEquals(67.00, foundPersonFour.getPrice());
    }

    @Test
    @Order(6)
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
                        .config(
                                RestAssuredConfig
                                        .config()
                                        .encoderConfig(EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(
                                                        TestConfigs.CONTENT_TYPE_YML,
                                                        ContentType.TEXT)))
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .accept(TestConfigs.CONTENT_TYPE_YML)
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
