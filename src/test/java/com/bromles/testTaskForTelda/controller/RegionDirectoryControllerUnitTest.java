package com.bromles.testTaskForTelda.controller;

import com.bromles.testTaskForTelda.entity.RegionDTO;
import com.bromles.testTaskForTelda.exception.DuplicateUniqueValuesException;
import com.bromles.testTaskForTelda.exception.RecordNotFoundException;
import com.bromles.testTaskForTelda.service.RegionDirectoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RegionDirectoryController.class)
public class RegionDirectoryControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RegionDirectoryService regionDirectoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String v1RegionsMapping = "/v1/regions";

    @Test
    public void add_PostRegionDTO_ReturnStatusOK_andReturnRegionDTO() throws Exception {
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
    public void add_PostEmptyRegionDTO_ReturnStatusBadRequest_andReturnValidationErrorMessages() throws Exception {
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
        violatedFields.put("id", 10);

        String requestBody = objectMapper.writeValueAsString(regionDTO);

        doThrow(new DuplicateUniqueValuesException(violatedFields)).when(regionDirectoryService).add(regionDTO);

        mvc.perform(
                post(v1RegionsMapping)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Duplicate primary key or unique index")))
                .andExpect(jsonPath("$.violated-fields.id", is(10)));
    }

    @Test
    public void getAll_GetRegionDTOs_ReturnJsonArray() throws Exception {
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
    public void getAll_GetNothing_ReturnStatusNotFound_andReturnErrorMessage() throws Exception {

        when(regionDirectoryService.getAll()).thenThrow(new RecordNotFoundException());

        mvc.perform(
                get(v1RegionsMapping)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("There are no records")));
    }

    @Test
    public void getByName_GetName_ReturnStatusOk_andReturnRegionDTO() throws Exception {
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
    public void getByName_GetNothing_ReturnStatusBadRequest_andReturnValidationErrorMessage() throws Exception {

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
    public void getByName_GetNothing_ReturnStatusNotFound_andReturnErrorMessage() throws Exception {

        when(regionDirectoryService.getByName("город Москва")).
                thenThrow(new RecordNotFoundException("name = 'город Москва'"));

        mvc.perform(
                get(v1RegionsMapping + "?name=город Москва")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("No records found by name = 'город Москва'")));
    }
}