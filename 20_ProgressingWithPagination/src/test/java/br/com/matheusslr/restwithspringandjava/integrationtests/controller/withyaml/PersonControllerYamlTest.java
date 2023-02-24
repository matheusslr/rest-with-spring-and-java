package br.com.matheusslr.restwithspringandjava.integrationtests.controller.withyaml;

import br.com.matheusslr.restwithspringandjava.configs.TestConfigs;
import br.com.matheusslr.restwithspringandjava.integrationtests.controller.withyaml.mapper.YamlMapper;
import br.com.matheusslr.restwithspringandjava.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.matheusslr.restwithspringandjava.integrationtests.vo.AccountCredentialsVO;
import br.com.matheusslr.restwithspringandjava.integrationtests.vo.PersonVO;
import br.com.matheusslr.restwithspringandjava.integrationtests.vo.pagedmodels.PagedModelPerson;
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
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YamlMapper objectMapper;

    private static PersonVO personVO;

    @BeforeAll
    public static void setup() {
        objectMapper = new YamlMapper();
        personVO = new PersonVO();
    }

    private void mockPerson() {
        personVO.setFirstName("Richard");
        personVO.setLastName("Rasmussen");
        personVO.setAddress("São Paulo - Brazil");
        personVO.setGender("Male");
        personVO.setEnabled(true);
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
                .setBasePath("/api/persons/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    public void testSave() throws IOException, JsonMappingException, JsonProcessingException {
        mockPerson();
        var created =
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
                        .queryParams("page", 0, "size", 12, "direction", "asc")
                        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUSSLR)
                        .body(personVO, objectMapper)
                        .when()
                        .post()
                        .then()
                        .statusCode(201)
                        .extract()
                        .body()
                        .as(PersonVO.class, objectMapper);

        personVO = created;

        assertNotNull(created.getId());
        assertNotNull(created.getFirstName());
        assertNotNull(created.getLastName());
        assertNotNull(created.getAddress());
        assertNotNull(created.getGender());
        assertTrue(created.getEnabled());

        assertEquals("Richard", created.getFirstName());
        assertEquals("Rasmussen", created.getLastName());
        assertEquals("São Paulo - Brazil", created.getAddress());
        assertEquals("Male", created.getGender());

        assertTrue(created.getId() > 0);
    }

    @Test
    @Order(3)
    public void testFindById() throws IOException, JsonMappingException, JsonProcessingException {
        mockPerson();

        var persistedPerson =
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
                        .queryParams("page", 0, "size", 12, "direction", "asc")
                        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUSSLR)
                        .pathParam("id", personVO.getId())
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(PersonVO.class, objectMapper);

        personVO = persistedPerson;

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertTrue(persistedPerson.getEnabled());

        assertEquals("Richard", persistedPerson.getFirstName());
        assertEquals("Rasmussen", persistedPerson.getLastName());
        assertEquals("São Paulo - Brazil", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());

        assertTrue(persistedPerson.getId() > 0);
    }

    @Test
    @Order(4)
    public void testUpdate() throws IOException {
        personVO.setLastName("testLastName");

        var persistedPerson = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfigs.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .body(personVO, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(PersonVO.class, objectMapper);

        personVO = persistedPerson;

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertTrue(persistedPerson.getEnabled());

        assertEquals("Richard", persistedPerson.getFirstName());
        assertEquals("testLastName", persistedPerson.getLastName());
        assertEquals("São Paulo - Brazil", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(5)
    public void testDisablePerson() throws IOException, JsonMappingException, JsonProcessingException {
        mockPerson();

        var persistedPerson =
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
                        .queryParams("page", 0, "size", 12, "direction", "asc")
                        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUSSLR)
                        .pathParam("id", personVO.getId())
                        .when()
                        .patch("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(PersonVO.class, objectMapper);

        personVO = persistedPerson;

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertFalse(persistedPerson.getEnabled());

        assertEquals("Richard", persistedPerson.getFirstName());
        assertEquals("testLastName", persistedPerson.getLastName());
        assertEquals("São Paulo - Brazil", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());

        assertTrue(persistedPerson.getId() > 0);
    }

    @Test
    @Order(6)
    public void testRemove() {
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
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .pathParam("id", personVO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(7)
    public void testFindAll() throws IOException {
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
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelPerson.class, objectMapper);

        List<PersonVO> people = content.getContent();

        PersonVO foundPersonOne = people.get(0);

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());

        assertEquals("Aaron", foundPersonOne.getFirstName());
        assertEquals("Oddy", foundPersonOne.getLastName());
        assertEquals("01 Colorado Court", foundPersonOne.getAddress());
        assertEquals("Male", foundPersonOne.getGender());

        PersonVO foundPersonFour = people.get(3);

        assertNotNull(foundPersonFour.getId());
        assertNotNull(foundPersonFour.getFirstName());
        assertNotNull(foundPersonFour.getLastName());
        assertNotNull(foundPersonFour.getAddress());
        assertNotNull(foundPersonFour.getGender());

        assertEquals("Abra", foundPersonFour.getFirstName());
        assertEquals("Thebe", foundPersonFour.getLastName());
        assertEquals("134 Raven Lane", foundPersonFour.getAddress());
        assertEquals("Female", foundPersonFour.getGender());
    }

    @Test
    @Order(8)
    public void testFindByName() throws IOException {
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
                .pathParam("firstName","ag")
                .when()
                .get("findPersonByName/{firstName}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelPerson.class, objectMapper);

        List<PersonVO> people = content.getContent();

        PersonVO foundPersonOne = people.get(0);

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());

        assertEquals("Aguste", foundPersonOne.getFirstName());
        assertEquals("Jorin", foundPersonOne.getLastName());
        assertEquals("8 Jenna Alley", foundPersonOne.getAddress());
        assertEquals("Male", foundPersonOne.getGender());

        PersonVO foundPersonFour = people.get(3);

        assertNotNull(foundPersonFour.getId());
        assertNotNull(foundPersonFour.getFirstName());
        assertNotNull(foundPersonFour.getLastName());
        assertNotNull(foundPersonFour.getAddress());
        assertNotNull(foundPersonFour.getGender());

        assertEquals("Aggi", foundPersonFour.getFirstName());
        assertEquals("Hugo", foundPersonFour.getLastName());
        assertEquals("5 Waxwing Court", foundPersonFour.getAddress());
        assertEquals("Female", foundPersonFour.getGender());
    }

    @Test
    @Order(9)
    public void testHATEOS() throws IOException {
        var unthreatedContent = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfigs.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var content = unthreatedContent.replace("\n", "").replace("\r", "");

        assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/persons/v1/698\""));
        assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/persons/v1/377\""));
        assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/persons/v1/189\""));

        assertTrue(content.contains("rel: \"first\"  href: \"http://localhost:8888/api/persons/v1?direction=asc&page=0&size=12&sort=firstName,asc\""));
        assertTrue(content.contains("rel: \"self\"  href: \"http://localhost:8888/api/persons/v1?page=0&size=12&direction=asc\""));
        assertTrue(content.contains("rel: \"next\"  href: \"http://localhost:8888/api/persons/v1?direction=asc&page=1&size=12&sort=firstName,asc\""));
        assertTrue(content.contains("rel: \"last\"  href: \"http://localhost:8888/api/persons/v1?direction=asc&page=83&size=12&sort=firstName,asc\""));

        assertTrue(content.contains("page:  size: 12  totalElements: 1006  totalPages: 84  number: 0"));
    }

    @Test
    @Order(10)
    public void testFindByIdWithoutToken() throws IOException, JsonMappingException, JsonProcessingException {
        mockPerson();

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
                        .pathParam("id", personVO.getId())
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(403)
                        .extract()
                        .body()
                        .asString();
    }
}
