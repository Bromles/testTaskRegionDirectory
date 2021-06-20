package com.bromles.test_task_region_directory;

import com.bromles.test_task_region_directory.controller.RegionDirectoryController;
import com.bromles.test_task_region_directory.controller.schema.SuccessfullyAddedOrUpdatedResponse;
import com.bromles.test_task_region_directory.controller.schema.SuccessfullyDeletedResponse;
import com.bromles.test_task_region_directory.entity.RegionDTO;
import com.bromles.test_task_region_directory.exception.DuplicateUniqueValuesException;
import com.bromles.test_task_region_directory.repository.IRegionRepository;
import com.bromles.test_task_region_directory.service.IRegionDirectoryService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestTaskRegionDirectoryIntegrationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private IRegionDirectoryService service;

    private static final String v1RegionsMapping = "/v1/regions";

    @AfterEach
    void resetDirectory() {
        try {
            List<RegionDTO> regions = service.getAll();

            for(RegionDTO regionDTO : regions) {
                service.deleteById(regionDTO.id);
            }
        }
        catch (Exception ignored) {
        }
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void add_PostValidRegionDTO_ReturnStatusOK_andReturnRegionDTO() {
        RegionDTO regionDTO = new RegionDTO("78", "город Москва", "МСК");

        ResponseEntity<SuccessfullyAddedOrUpdatedResponse> response = testRestTemplate.postForEntity(v1RegionsMapping,
                regionDTO, SuccessfullyAddedOrUpdatedResponse.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getSuccessful(), is(true));
        assertThat(response.getBody().getValue().id, is(regionDTO.id));
        assertThat(response.getBody().getValue().name, is(regionDTO.name));
        assertThat(response.getBody().getValue().shortName, is(regionDTO.shortName));
    }

    @Test
    public void add_PostIllegalRegionDTO_ReturnStatusBadRequest_andReturnValidationErrorMessages() {
        RegionDTO regionDTO = new RegionDTO();

        ResponseEntity<String> response = testRestTemplate.postForEntity(v1RegionsMapping, regionDTO, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.errors[0]"),
                anyOf(is("Region code cannot be blank"), is("Region name cannot be blank")));
        assertThat(JsonPath.read(body, "$.errors[1]"),
                anyOf(is("Region code cannot be blank"), is("Region name cannot be blank")));
    }

    @Test
    public void add_PostEmptyBody_ReturnStatusBadRequest_andReturnErrorMessage() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                testRestTemplate.exchange(v1RegionsMapping, HttpMethod.POST, request, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.message"), is("Unformed JSON in request body"));
    }

    @Test
    public void add_PostRegionDTOWithExistingId_ReturnStatusBadRequest_andReturnErrorMessage() throws DuplicateUniqueValuesException {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");

        service.add(regionDTO);

        ResponseEntity<String> response = testRestTemplate.postForEntity(v1RegionsMapping, regionDTO, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.message"), is("Duplicate primary key or unique index"));
        assertThat(JsonPath.read(body, "$.violated-fields.id"), is(regionDTO.id));
    }

    @Test
    public void add_PostPlainText_ReturnStatusUnsupportedMediaType_andReturnErrorMessage() {
        RegionDTO regionDTO = new RegionDTO("78", "город Москва", "МСК");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> request = new HttpEntity<>(regionDTO.toString(), headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity(v1RegionsMapping, request, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.UNSUPPORTED_MEDIA_TYPE));
        assertThat(JsonPath.read(body, "$.message"), is("Server supports only application/json"));
    }

    @Test
    public void getAll_GetNothing_ReturnStatusOk_andReturnRegionDTOs() throws DuplicateUniqueValuesException {
        RegionDTO regionDTO = new RegionDTO("78", "город Москва", "МСК");

        service.add(regionDTO);

        ResponseEntity<String> response = testRestTemplate.getForEntity(v1RegionsMapping, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(JsonPath.read(body, "$"), hasSize(1));
        assertThat(JsonPath.read(body, "$[0].id"), is(regionDTO.id));
        assertThat(JsonPath.read(body, "$[0].name"), is(regionDTO.name));
        assertThat(JsonPath.read(body, "$[0].shortName"), is(regionDTO.shortName));
    }

    @Test
    public void getAll_GetNotExisting_ReturnStatusNotFound_andReturnErrorMessage() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(v1RegionsMapping, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(JsonPath.read(body, "$.message"), is("There are no records"));
    }

    @Test
    public void getByName_GetValidName_ReturnStatusOk_andReturnRegionDTOs() throws DuplicateUniqueValuesException {
        String name = "город Москва";
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");

        service.add(regionDTO);

        ResponseEntity<String> response = testRestTemplate.getForEntity(v1RegionsMapping + "?name=" + name,
                String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(JsonPath.read(body, "$"), hasSize(1));
        assertThat(JsonPath.read(body, "$[0].id"), is(regionDTO.id));
        assertThat(JsonPath.read(body, "$[0].name"), is(regionDTO.name));
        assertThat(JsonPath.read(body, "$[0].shortName"), is(regionDTO.shortName));
    }

    @Test
    public void getByName_GetEmptyName_ReturnStatusBadRequest_andReturnValidationErrorMessages() {

        ResponseEntity<String> response = testRestTemplate.getForEntity(v1RegionsMapping + "?name=", String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.errors[0]"),
                anyOf(is("Region name must contain only Cyrillic, spaces, dashes and brackets"),
                        is("Region name can't be blank")));
        assertThat(JsonPath.read(body, "$.errors[1]"),
                anyOf(is("Region name must contain only Cyrillic, spaces, dashes and brackets"),
                        is("Region name can't be blank")));
    }

    @Test
    public void getByName_GetIllegalName_ReturnStatusBadRequest_andReturnValidationErrorMessage() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(v1RegionsMapping + "?name=123", String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.errors[0]"),
                is("Region name must contain only Cyrillic, spaces, dashes and brackets"));
    }

    @Test
    public void getByName_GetNotExistingName_ReturnStatusNotFound_andReturnErrorMessage() {
        String name = "город Москва";

        ResponseEntity<String> response = testRestTemplate.getForEntity(v1RegionsMapping + "?name=" + name,
                String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(JsonPath.read(body, "$.message"), is("No records found by name = '" + name + "'"));
    }

    @Test
    public void getByNameBeginning_GetValidNameBeginning_ReturnStatusOk_andReturnRegionDTOs() throws DuplicateUniqueValuesException {
        RegionDTO regionDTO1 = new RegionDTO("10", "Вологодская область", "ВОЛ");
        RegionDTO regionDTO2 = new RegionDTO("11", "Волгоградская область", "ВЛГ");
        String nameBeginning = "Вол";

        service.add(regionDTO1);
        service.add(regionDTO2);

        ResponseEntity<String> response =
                testRestTemplate.getForEntity(v1RegionsMapping + "?name-beginning=" + nameBeginning, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(JsonPath.read(body, "$"), hasSize(2));
        assertThat(JsonPath.read(body, "$[0].id"), is(regionDTO2.id));
        assertThat(JsonPath.read(body, "$[0].name"), is(regionDTO2.name));
        assertThat(JsonPath.read(body, "$[0].shortName"), is(regionDTO2.shortName));
        assertThat(JsonPath.read(body, "$[1].id"), is(regionDTO1.id));
        assertThat(JsonPath.read(body, "$[1].name"), is(regionDTO1.name));
        assertThat(JsonPath.read(body, "$[1].shortName"), is(regionDTO1.shortName));
    }

    @Test
    public void getByNameBeginning_GetIllegalNameBeginning_ReturnStatusBadRequest_andReturnValidationErrorMessages() {
        String nameBeginning = "123";

        ResponseEntity<String> response =
                testRestTemplate.getForEntity(v1RegionsMapping + "?name-beginning=" + nameBeginning, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.errors[0]"),
                is("Beginning of region name can't be blank, must contain only Cyrillic letters and begins with " +
                        "Capital one"));
    }

    @Test
    public void getByNameBeginning_GetEmptyNameBeginning_ReturnStatusBadRequest_andReturnValidationErrorMessages() {

        ResponseEntity<String> response = testRestTemplate.getForEntity(v1RegionsMapping + "?name-beginning=", String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.errors[0]"),
                is("Beginning of region name can't be blank, must contain only Cyrillic letters and begins with " +
                        "Capital one"));
    }

    @Test
    public void getByNameBeginning_GetNotExistingNameBeginning_ReturnStatusNotFound_andReturnErrorMessage() {
        String nameBeginning = "Вол";

        ResponseEntity<String> response = testRestTemplate.getForEntity(v1RegionsMapping + "?name-beginning=" + nameBeginning,
                String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(JsonPath.read(body, "$.message"),
                is("No records found by name beginning = '" + nameBeginning + "'"));
    }

    @Test
    public void getByShortName_GetValidShortName_ReturnStatusOk_andReturnRegionDTOs() throws DuplicateUniqueValuesException {
        String shortName = "МСК";

        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");

        service.add(regionDTO);

        ResponseEntity<String> response = testRestTemplate.getForEntity(v1RegionsMapping + "?short-name=" + shortName,
                String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(JsonPath.read(body, "$[0].id"), is(regionDTO.id));
        assertThat(JsonPath.read(body, "$[0].name"), is(regionDTO.name));
        assertThat(JsonPath.read(body, "$[0].shortName"), is(regionDTO.shortName));
    }

    @Test
    public void getByShortName_GetIllegalShortName_ReturnStatusBadRequest_andReturnValidationErrorMessages() {
        String shortName = "123";

        ResponseEntity<String> response = testRestTemplate.getForEntity(v1RegionsMapping + "?short-name=" + shortName,
                String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.errors[0]"),
                is("Region short name can't be blank and must be 3 Capital Cyrillic letters"));
    }

    @Test
    public void getByShortName_GetEmptyShortName_ReturnStatusBadRequest_andReturnValidationErrorMessages() {

        ResponseEntity<String> response = testRestTemplate.getForEntity(v1RegionsMapping + "?short-name=", String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.errors[0]"),
                is("Region short name can't be blank and must be 3 Capital Cyrillic letters"));
    }

    @Test
    public void getByShortName_GetNotExistingShortName_ReturnStatusNotFound_andReturnErrorMessage() {
        String shortName = "МСК";

        ResponseEntity<String> response = testRestTemplate.getForEntity(v1RegionsMapping + "?short-name=" + shortName,
                String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(JsonPath.read(body, "$.message"),
                is("No records found by short name = '" + shortName + "'"));
    }

    @Test
    public void getById_GetValidId_ReturnStatusOk_andReturnRegionDTO() throws DuplicateUniqueValuesException {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");

        service.add(regionDTO);

        ResponseEntity<String> response =
                testRestTemplate.getForEntity(v1RegionsMapping + "/" + regionDTO.id, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(JsonPath.read(body, "$[0].id"), is(regionDTO.id));
        assertThat(JsonPath.read(body, "$[0].name"), is(regionDTO.name));
        assertThat(JsonPath.read(body, "$[0].shortName"), is(regionDTO.shortName));
    }

    @Test
    public void getById_GetIllegalId_ReturnStatusBadRequest_andReturnValidationErrorMessages() {
        String id = "00";

        ResponseEntity<String> response = testRestTemplate.getForEntity(v1RegionsMapping + "/" + id, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.errors[0]"),
                is("Region id must be 2 or 3 digits and mustn't contain only zeros"));
    }

    @Test
    public void getById_GetNotExistingId_ReturnStatusNotFound_andReturnValidationErrorMessages() {
        String id = "10";

        ResponseEntity<String> response = testRestTemplate.getForEntity(v1RegionsMapping + "/" + id, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(JsonPath.read(body, "$.message"), is("No records found by id = '" + id + "'"));
    }

    @Test
    public void updateById_UpdateValidIdAndValidRegionDTO_ReturnStatusOk_andReturnRegionDTO() throws DuplicateUniqueValuesException {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");
        String id = "10";

        service.add(regionDTO);

        HttpEntity<RegionDTO> request = new HttpEntity<>(regionDTO);

        ResponseEntity<SuccessfullyAddedOrUpdatedResponse> response =
                testRestTemplate.exchange(v1RegionsMapping + "/" + id, HttpMethod.PUT, request,
                        SuccessfullyAddedOrUpdatedResponse.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getSuccessful(), is(true));
        assertThat(response.getBody().getValue().id, is(regionDTO.id));
        assertThat(response.getBody().getValue().name, is(regionDTO.name));
        assertThat(response.getBody().getValue().shortName, is(regionDTO.shortName));
    }

    @Test
    public void updateById_UpdateValidIdAndRegionDTOWithExistingId_ReturnStatusBadRequest_andReturnErrorMessage() throws DuplicateUniqueValuesException {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");
        String id = "10";

        service.add(regionDTO);

        HttpEntity<RegionDTO> request = new HttpEntity<>(regionDTO);

        ResponseEntity<String> response =
                testRestTemplate.exchange(v1RegionsMapping + "/" + id, HttpMethod.PUT, request, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.message"), is("Duplicate primary key or unique index"));
        assertThat(JsonPath.read(body, "$.violated-fields.id"), is(id));
    }

    @Test
    public void updateById_UpdateIllegalIdAndValidRegionDTO_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws DuplicateUniqueValuesException {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");
        String id = "00";

        service.add(regionDTO);

        HttpEntity<RegionDTO> request = new HttpEntity<>(regionDTO);

        ResponseEntity<String> response =
                testRestTemplate.exchange(v1RegionsMapping + "/" + id, HttpMethod.PUT, request, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.errors[0]"),
                is("Region id must be 2 or 3 digits and mustn't contain only zeros"));
    }

    @Test
    public void updateById_UpdateEmptyIdAndValidRegionDTO_ReturnStatusMethodNotAllowed() throws DuplicateUniqueValuesException {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");

        service.add(regionDTO);

        HttpEntity<RegionDTO> request = new HttpEntity<>(regionDTO);

        ResponseEntity<String> response =
                testRestTemplate.exchange(v1RegionsMapping + "/", HttpMethod.PUT, request, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
    }

    @Test
    public void updateById_UpdateValidIdAndEmptyBody_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws DuplicateUniqueValuesException {
        String id = "10";
        RegionDTO regionDTO = new RegionDTO("10", "Москва", "МСК");

        service.add(regionDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                testRestTemplate.exchange(v1RegionsMapping + "/" + id, HttpMethod.PUT, request, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.message"), is("Unformed JSON in request body"));
    }

    @Test
    public void updateById_UpdateValidIdAndIllegalRegionDTO_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws DuplicateUniqueValuesException {
        RegionDTO regionDTO = new RegionDTO();
        RegionDTO regionDTO1 = new RegionDTO("10", "Москва", "МСК");
        String id = "10";

        service.add(regionDTO1);

        HttpEntity<RegionDTO> request = new HttpEntity<>(regionDTO);

        ResponseEntity<String> response =
                testRestTemplate.exchange(v1RegionsMapping + "/" + id, HttpMethod.PUT, request, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.errors[0]"),
                anyOf(is("Region code cannot be blank"), is("Region name cannot be blank")));
        assertThat(JsonPath.read(body, "$.errors[1]"),
                anyOf(is("Region code cannot be blank"), is("Region name cannot be blank")));
    }

    @Test
    public void updateById_UpdateNotExistingIdAndValidRegionDTO_ReturnStatusNotFound_andReturnErrorMessage() {
        String id = "10";
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");

        HttpEntity<RegionDTO> request = new HttpEntity<>(regionDTO);

        ResponseEntity<String> response =
                testRestTemplate.exchange(v1RegionsMapping + "/" + id, HttpMethod.PUT, request, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(JsonPath.read(body, "$.message"), is("No records found by id = '" + id + "'"));
    }

    @Test
    public void updateById_UpdateValidIdAndPlainText_ReturnStatusUnsupportedMediaType_andReturnErrorMessage() throws DuplicateUniqueValuesException {
        String id = "10";
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");

        service.add(regionDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> request = new HttpEntity<>(regionDTO.toString(), headers);

        ResponseEntity<String> response =
                testRestTemplate.exchange(v1RegionsMapping + "/" + id, HttpMethod.PUT, request, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.UNSUPPORTED_MEDIA_TYPE));
        assertThat(JsonPath.read(body, "$.message"), is("Server supports only application/json"));
    }

    @Test
    public void deleteById_DeleteValidId_ReturnStatusOk_andReturnMessage() throws DuplicateUniqueValuesException {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");

        service.add(regionDTO);

        HttpEntity<RegionDTO> request = new HttpEntity<>(null);
        ResponseEntity<SuccessfullyDeletedResponse> response =
                testRestTemplate.exchange(v1RegionsMapping + "/" + regionDTO.id, HttpMethod.DELETE, request,
                        SuccessfullyDeletedResponse.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getSuccessful(), is(true));
    }

    @Test
    public void deleteById_DeleteIllegalId_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws DuplicateUniqueValuesException {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");

        service.add(regionDTO);

        HttpEntity<RegionDTO> request = new HttpEntity<>(null);
        ResponseEntity<String> response =
                testRestTemplate.exchange(v1RegionsMapping + "/00", HttpMethod.DELETE, request, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(JsonPath.read(body, "$.errors[0]"),
                is("Region id must be 2 or 3 digits and mustn't contain only zeros"));
    }

    @Test
    public void deleteById_DeleteEmptyId_ReturnStatusMethodNotAllowed() throws DuplicateUniqueValuesException {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");

        service.add(regionDTO);

        HttpEntity<RegionDTO> request = new HttpEntity<>(null);
        ResponseEntity<String> response =
                testRestTemplate.exchange(v1RegionsMapping + "/", HttpMethod.DELETE, request, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
    }

    @Test
    public void deleteById_DeleteNotExistingId_ReturnStatusNotFound_andReturnErrorMessage() {
        String id = "10";

        HttpEntity<RegionDTO> request = new HttpEntity<>(null);
        ResponseEntity<String> response =
                testRestTemplate.exchange(v1RegionsMapping + "/" + id, HttpMethod.DELETE, request, String.class);
        String body = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(JsonPath.read(body, "$.message"), is("No records found by id = '" + id + "'"));
    }
}
