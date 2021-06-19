package com.bromles.test_task_region_directory.controller;

import com.bromles.test_task_region_directory.entity.RegionDTO;
import com.bromles.test_task_region_directory.exception.DuplicateUniqueValuesException;
import com.bromles.test_task_region_directory.exception.RecordNotFoundException;
import com.bromles.test_task_region_directory.service.RegionDirectoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegionDirectoryController.class)
public class RegionDirectoryControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RegionDirectoryService regionDirectoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String v1RegionsMapping = "/v1/regions";

    @Test
    public void add_PostValidRegionDTO_ReturnStatusOK_andReturnRegionDTO() throws Exception {
        RegionDTO regionDTO = new RegionDTO("78", "город Москва", "МСК");

        String requestBody = objectMapper.writeValueAsString(regionDTO);

        mvc.perform(
                post(v1RegionsMapping)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful", is(true)))
                .andExpect(jsonPath("$.value.id", is(regionDTO.id)))
                .andExpect(jsonPath("$.value.name", is(regionDTO.name)))
                .andExpect(jsonPath("$.value.shortName", is(regionDTO.shortName)));
    }

    @Test
    public void add_PostIllegalRegionDTO_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws Exception {
        RegionDTO regionDTO = new RegionDTO();

        String requestBody = objectMapper.writeValueAsString(regionDTO);

        mvc.perform(
                post(v1RegionsMapping)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors[0]",
                        anyOf(is("Region code cannot be blank"), is("Region name cannot be blank"))))
                .andExpect(jsonPath("$.errors[1]",
                        anyOf(is("Region code cannot be blank"), is("Region name cannot be blank"))));
    }

    @Test
    public void add_PostEmptyBody_ReturnStatusBadRequest_andReturnErrorMessage() throws Exception {
        mvc.perform(
                post(v1RegionsMapping)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Unformed JSON in request body")));
    }

    @Test
    public void add_PostRegionDTOWithExistingId_ReturnStatusBadRequest_andReturnErrorMessage() throws Exception {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");

        Map<String, Object> violatedFields = new HashMap<>();
        String id = "10";
        violatedFields.put("id", id);

        String requestBody = objectMapper.writeValueAsString(regionDTO);

        doThrow(new DuplicateUniqueValuesException(violatedFields)).when(regionDirectoryService).add(regionDTO);

        mvc.perform(
                post(v1RegionsMapping)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Duplicate primary key or unique index")))
                .andExpect(jsonPath("$.violated-fields.id", is(id)));
    }

    @Test
    public void add_PostPlainText_ReturnStatusUnsupportedMediaType_andReturnErrorMessage() throws Exception {
        RegionDTO regionDTO = new RegionDTO("78", "город Москва", "МСК");

        String requestBody = objectMapper.writeValueAsString(regionDTO);

        mvc.perform(
                post(v1RegionsMapping)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(requestBody))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.status", is(415)))
                .andExpect(jsonPath("$.message", is("Server supports only application/json")));
    }

    @Test
    public void getAll_GetNothing_ReturnStatusOk_andReturnRegionDTOs() throws Exception {
        RegionDTO regionDTO = new RegionDTO("78", "город Москва", "МСК");

        List<RegionDTO> allRegions = Collections.singletonList(regionDTO);

        when(regionDirectoryService.getAll()).thenReturn(allRegions);

        mvc.perform(
                get(v1RegionsMapping)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(regionDTO.id)))
                .andExpect(jsonPath("$[0].name", is(regionDTO.name)))
                .andExpect(jsonPath("$[0].shortName", is(regionDTO.shortName)));
    }

    @Test
    public void getAll_GetNotExisting_ReturnStatusNotFound_andReturnErrorMessage() throws Exception {

        when(regionDirectoryService.getAll()).thenThrow(new RecordNotFoundException());

        mvc.perform(
                get(v1RegionsMapping)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("There are no records")));
    }

    @Test
    public void getByName_GetValidName_ReturnStatusOk_andReturnRegionDTOs() throws Exception {
        String name = "город Москва";
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");
        List<RegionDTO> allRegions = Collections.singletonList(regionDTO);

        when(regionDirectoryService.getByName(name)).thenReturn(allRegions);

        mvc.perform(
                get(v1RegionsMapping + "?name=" + name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(regionDTO.id)))
                .andExpect(jsonPath("$[0].name", is(regionDTO.name)))
                .andExpect(jsonPath("$[0].shortName", is(regionDTO.shortName)));
    }

    @Test
    public void getByName_GetEmptyName_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws Exception {

        mvc.perform(
                get(v1RegionsMapping + "?name=")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors[0]",
                        anyOf(is("Region name must contain only Cyrillic, spaces, dashes and brackets"),
                                is("Region name can't be blank"))))
                .andExpect(jsonPath("$.errors[1]",
                        anyOf(is("Region name must contain only Cyrillic, spaces, dashes and brackets"),
                                is("Region name can't be blank"))));
    }

    @Test
    public void getByName_GetIllegalName_ReturnStatusBadRequest_andReturnValidationErrorMessage() throws Exception {
        mvc.perform(
                get(v1RegionsMapping + "?name=123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors[0]",
                        is("Region name must contain only Cyrillic, spaces, dashes and brackets")));
    }

    @Test
    public void getByName_GetNotExistingName_ReturnStatusNotFound_andReturnErrorMessage() throws Exception {

        String name = "город Москва";

        when(regionDirectoryService.getByName(name)).
                thenThrow(new RecordNotFoundException("name = '" + name + "'"));

        mvc.perform(
                get(v1RegionsMapping + "?name=" + name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("No records found by name = '" + name + "'")));
    }

    @Test
    public void getByNameBeginning_GetValidNameBeginning_ReturnStatusOk_andReturnRegionDTOs() throws Exception {
        RegionDTO regionDTO1 = new RegionDTO("10", "Вологодская область", "ВОЛ");
        RegionDTO regionDTO2 = new RegionDTO("11", "Волгоградская область", "ВЛГ");
        String nameBeginning = "Вол";

        List<RegionDTO> regionDTOs = new ArrayList<>();
        regionDTOs.add(regionDTO1);
        regionDTOs.add(regionDTO2);

        when(regionDirectoryService.getByNameBeginning(nameBeginning)).thenReturn(regionDTOs);

        mvc.perform(
                get(v1RegionsMapping + "?name-beginning=" + nameBeginning)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(regionDTO1.id)))
                .andExpect(jsonPath("$[0].name", is(regionDTO1.name)))
                .andExpect(jsonPath("$[0].shortName", is(regionDTO1.shortName)))
                .andExpect(jsonPath("$[1].id", is(regionDTO2.id)))
                .andExpect(jsonPath("$[1].name", is(regionDTO2.name)))
                .andExpect(jsonPath("$[1].shortName", is(regionDTO2.shortName)));
    }

    @Test
    public void getByNameBeginning_GetIllegalNameBeginning_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws Exception {
        String nameBeginning = "123";

        mvc.perform(
                get(v1RegionsMapping + "?name-beginning=" + nameBeginning)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors[0]",
                        is("Beginning of region name can't be blank, must contain only Cyrillic letters and begins with Capital one")));
    }

    @Test
    public void getByNameBeginning_GetEmptyNameBeginning_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws Exception {

        mvc.perform(
                get(v1RegionsMapping + "?name-beginning=")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors[0]",
                        is("Beginning of region name can't be blank, must contain only Cyrillic letters and begins with Capital one")));
    }

    @Test
    public void getByNameBeginning_GetNotExistingNameBeginning_ReturnStatusNotFound_andReturnErrorMessage() throws Exception {
        String nameBeginning = "Вол";

        when(regionDirectoryService.getByNameBeginning(nameBeginning)).
                thenThrow(new RecordNotFoundException("name beginning = '" + nameBeginning + "'"));

        mvc.perform(
                get(v1RegionsMapping + "?name-beginning=" + nameBeginning)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message",
                        is("No records found by name beginning = '" + nameBeginning + "'")));
    }

    @Test
    public void getByShortName_GetValidShortName_ReturnStatusOk_andReturnRegionDTOs() throws Exception {
        String shortName = "МСК";

        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");
        List<RegionDTO> allRegions = Collections.singletonList(regionDTO);

        when(regionDirectoryService.getByShortName(shortName)).thenReturn(allRegions);

        mvc.perform(
                get(v1RegionsMapping + "?short-name=" + shortName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(regionDTO.id)))
                .andExpect(jsonPath("$[0].name", is(regionDTO.name)))
                .andExpect(jsonPath("$[0].shortName", is(regionDTO.shortName)));
    }

    @Test
    public void getByShortName_GetIllegalShortName_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws Exception {
        String shortName = "123";

        mvc.perform(
                get(v1RegionsMapping + "?short-name=" + shortName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors[0]",
                        is("Region short name can't be blank and must be 3 Capital Cyrillic letters")));
    }

    @Test
    public void getByShortName_GetEmptyShortName_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws Exception {

        mvc.perform(
                get(v1RegionsMapping + "?short-name=")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors[0]",
                        is("Region short name can't be blank and must be 3 Capital Cyrillic letters")));
    }

    @Test
    public void getByShortName_GetNotExistingShortName_ReturnStatusNotFound_andReturnErrorMessage() throws Exception {
        String shortName = "МСК";

        when(regionDirectoryService.getByShortName(shortName)).
                thenThrow(new RecordNotFoundException("short name = '" + shortName + "'"));

        mvc.perform(
                get(v1RegionsMapping + "?short-name=" + shortName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("No records found by short name = '" + shortName + "'")));
    }

    @Test
    public void getById_GetValidId_ReturnStatusOk_andReturnRegionDTO() throws Exception {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");
        String id = "10";

        when(regionDirectoryService.getById(id)).thenReturn(regionDTO);

        mvc.perform(
                get(v1RegionsMapping + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(regionDTO.id)))
                .andExpect(jsonPath("$.name", is(regionDTO.name)))
                .andExpect(jsonPath("$.shortName", is(regionDTO.shortName)));
    }

    @Test
    public void getById_GetIllegalId_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws Exception {
        String id = "00";

        mvc.perform(
                get(v1RegionsMapping + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]",
                        is("Region id must be 2 or 3 digits and mustn't contain only zeros")));
    }

    @Test
    public void getById_GetNotExistingId_ReturnStatusNotFound_andReturnValidationErrorMessages() throws Exception {
        String id = "10";

        when(regionDirectoryService.getById(id)).
                thenThrow(new RecordNotFoundException("id = '" + id + "'"));

        mvc.perform(
                get(v1RegionsMapping + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("No records found by id = '" + id + "'")));
    }

    @Test
    public void updateById_UpdateValidIdAndValidRegionDTO_ReturnStatusOk_andReturnRegionDTO() throws Exception {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");
        String id = "10";

        String requestBody = objectMapper.writeValueAsString(regionDTO);

        mvc.perform(
                put(v1RegionsMapping + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful", is(true)))
                .andExpect(jsonPath("$.value.id", is(regionDTO.id)))
                .andExpect(jsonPath("$.value.name", is(regionDTO.name)))
                .andExpect(jsonPath("$.value.shortName", is(regionDTO.shortName)));
    }

    @Test
    public void updateById_UpdateValidIdAndRegionDTOWithExistingId_ReturnStatusBadRequest_andReturnErrorMessage() throws Exception {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");
        String id = "10";

        String requestBody = objectMapper.writeValueAsString(regionDTO);

        Map<String, Object> violatedFields = new HashMap<>();
        violatedFields.put("id", id);

        doThrow(new DuplicateUniqueValuesException(violatedFields)).when(regionDirectoryService).updateById(id, regionDTO);

        mvc.perform(
                put(v1RegionsMapping + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Duplicate primary key or unique index")))
                .andExpect(jsonPath("$.violated-fields.id", is(id)));
    }

    @Test
    public void updateById_UpdateIllegalIdAndValidRegionDTO_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws Exception {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");
        String id = "00";

        String requestBody = objectMapper.writeValueAsString(regionDTO);

        mvc.perform(
                put(v1RegionsMapping + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors[0]",
                        is("Region id must be 2 or 3 digits and mustn't contain only zeros")));
    }

    @Test
    public void updateById_UpdateEmptyIdAndValidRegionDTO_ReturnStatusMethodNotAllowed() throws Exception {
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");

        String requestBody = objectMapper.writeValueAsString(regionDTO);

        mvc.perform(
                put(v1RegionsMapping + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void updateById_UpdateValidIdAndEmptyBody_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws Exception {
        String id = "10";

        mvc.perform(
                put(v1RegionsMapping + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Unformed JSON in request body")));
    }

    @Test
    public void updateById_UpdateValidIdAndIllegalRegionDTO_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws Exception {
        RegionDTO regionDTO = new RegionDTO();
        String id = "10";

        String requestBody = objectMapper.writeValueAsString(regionDTO);

        mvc.perform(
                put(v1RegionsMapping + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors[0]",
                        anyOf(is("Region code cannot be blank"), is("Region name cannot be blank"))))
                .andExpect(jsonPath("$.errors[1]",
                        anyOf(is("Region code cannot be blank"), is("Region name cannot be blank"))));
    }

    @Test
    public void updateById_UpdateNotExistingIdAndValidRegionDTO_ReturnStatusNotFound_andReturnErrorMessage() throws Exception {
        String id = "10";
        RegionDTO regionDTO = new RegionDTO("10", "город Москва", "МСК");

        doThrow(new RecordNotFoundException("id = '" + id + "'")).when(regionDirectoryService).updateById(id,
                regionDTO);

        String requestBody = objectMapper.writeValueAsString(regionDTO);

        mvc.perform(
                put(v1RegionsMapping + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("No records found by id = '" + id + "'")));
    }

    @Test
    public void updateById_UpdateValidIdAndPlainText_ReturnStatusUnsupportedMediaType_andReturnErrorMessage() throws Exception {
        String id = "10";
        RegionDTO regionDTO = new RegionDTO("78", "город Москва", "МСК");

        String requestBody = objectMapper.writeValueAsString(regionDTO);

        mvc.perform(
                put(v1RegionsMapping + "/" + id)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(requestBody))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.status", is(415)))
                .andExpect(jsonPath("$.message", is("Server supports only application/json")));
    }

    @Test
    public void deleteById_DeleteValidId_ReturnStatusOk_andReturnMessage() throws Exception {
        mvc.perform(
                delete(v1RegionsMapping + "/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful", is(true)));
    }

    @Test
    public void deleteById_DeleteIllegalId_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws Exception {
        mvc.perform(
                delete(v1RegionsMapping + "/00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors[0]",
                        is("Region id must be 2 or 3 digits and mustn't contain only zeros")));
    }

    @Test
    public void deleteById_DeleteEmptyId_ReturnStatusMethodNotAllowed() throws Exception {
        mvc.perform(
                delete(v1RegionsMapping + "/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void deleteById_DeleteNotExistingId_ReturnStatusNotFound_andReturnErrorMessage() throws Exception {
        String id = "10";

        doThrow(new RecordNotFoundException("id = '" + id + "'")).when(regionDirectoryService).deleteById(id);

        mvc.perform(
                delete(v1RegionsMapping + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("No records found by id = '" + id + "'")));
    }
}