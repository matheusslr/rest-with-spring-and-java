package br.com.matheusslr.restwithspringandjava.integrationtests.controller.withjson;

import br.com.matheusslr.restwithspringandjava.configs.TestConfigs;
import br.com.matheusslr.restwithspringandjava.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.matheusslr.restwithspringandjava.integrationtests.vo.PersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonVO personVO;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        personVO = new PersonVO();
    }

    private void mockPerson() {
        personVO.setFirstName("Richard");
        personVO.setLastName("Rasmussen");
        personVO.setAddress("São Paulo - Brazil");
        personVO.setGender("Male");
    }

    @Test
    @Order(1)
    public void testSave() throws IOException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUSSLR)
                .setBasePath("/api/persons/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content =
                given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                            .body(personVO)
                        .when()
                            .post()
                        .then()
                            .statusCode(201)
                                .extract()
                                    .body()
                                        .asString();

        PersonVO created = objectMapper.readValue(content, PersonVO.class);
        personVO = created;

        assertNotNull(created.getId());
        assertNotNull(created.getFirstName());
        assertNotNull(created.getLastName());
        assertNotNull(created.getAddress());
        assertNotNull(created.getGender());

        assertEquals("Richard", created.getFirstName());
        assertEquals("Rasmussen", created.getLastName());
        assertEquals("São Paulo - Brazil", created.getAddress());
        assertEquals("Male", created.getGender());

        assertTrue(created.getId() > 0);
    }

    @Test
    @Order(2)
    public void testSaveWithWrongOrigin() throws IOException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .setBasePath("/api/persons/v1")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content =
                given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                            .body(personVO)
                        .when()
                            .post()
                        .then()
                            .statusCode(403)
                                .extract()
                                    .body()
                                        .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    public void testFindById() throws IOException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUSSLR)
                .setBasePath("/api/persons/v1")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content =
                given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                            .pathParam("id", personVO.getId())
                        .when()
                            .get("{id}")
                        .then()
                            .statusCode(200)
                                .extract()
                                    .body()
                                        .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        personVO = persistedPerson;

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());

        assertEquals("Richard", persistedPerson.getFirstName());
        assertEquals("Rasmussen", persistedPerson.getLastName());
        assertEquals("São Paulo - Brazil", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());

        assertTrue(persistedPerson.getId() > 0);
    }

    @Test
    @Order(4)
    public void testFindByIdWithWrongOrigin() throws IOException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .setBasePath("/api/persons/v1")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content =
                given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                            .pathParam("id", personVO.getId())
                        .when()
                            .get("{id}")
                        .then()
                            .statusCode(403)
                                .extract()
                                    .body()
                                        .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }
}
