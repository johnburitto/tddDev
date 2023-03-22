package com.johnburitto.tdddev.controller;

import com.johnburitto.tdddev.dto.PatientDto;
import com.johnburitto.tdddev.model.Patient;
import com.johnburitto.tdddev.service.PatientService;
import com.johnburitto.tdddev.utils.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PatientIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PatientService service;

    @Test
    void isShouldGetAllPatient() throws Exception {
        //Given
        var patients = service.getAll();

        //Then
        var result = mockMvc.perform(get("http://localhost:8080/api/v1/patients/"));
        var resultPatients = JsonUtil.listFromJson(result.andReturn().getResponse().getContentAsString(), Patient.class);

        //When
        result.andExpect(status().isOk());
        assertThat(resultPatients).isNotNull();
        assertThat(resultPatients).isNotEmpty();
        assertThat(resultPatients).isEqualTo(patients);
    }

    @Test
    void isShouldGetPatientById() throws Exception {
        //Given
        var patient = service.getById("63ff3f35cb28c75bc12f1a6c");

        //Then
        var result = mockMvc.perform(get("http://localhost:8080/api/v1/patients/63ff3f35cb28c75bc12f1a6c"));
        var resultPatient = JsonUtil.fromJson(result.andReturn().getResponse().getContentAsString(), Patient.class);

        //When
        result.andExpect(status().isOk());
        assertThat(resultPatient).isNotNull();
        assertThat(resultPatient).isEqualTo(patient);
    }

    @Test
    void itShouldRegisterPatient() throws Exception {
        //Given
        var patientDto = new PatientDto("John", "(611)67-890", "john@gmail.com");

        //When
        var result = mockMvc.perform(post("http://localhost:8080/api/v1/patients/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(patientDto)));

        //Then
        result.andExpect(status().isOk());
        assertThat(service.getByPhoneNumber("(611)67-890")).isNotNull();
        assertThat(service.getByPhoneNumber("(611)67-890").getName()).isEqualTo(patientDto.getName());
        assertThat(service.getByPhoneNumber("(611)67-890").getId()).isNotNull();
        assertThat(service.getByPhoneNumber("(611)67-890").getId()).isNotEmpty();
    }

    @Test
    void itShouldUpdatePatient() throws Exception {
        //Given
        var oldPatient = service.getByPhoneNumber("(611)67-890");
        var patientDto = new PatientDto("New John", "(611)67-890", "john@gmail.com");

        //When
        var result = mockMvc.perform(put(String.format("http://localhost:8080/api/v1/patients/update/%s", oldPatient.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(patientDto)));

        //Then
        result.andExpect(status().isOk());
        assertThat(service.getByPhoneNumber("(611)67-890")).isNotNull();
        assertThat(service.getByPhoneNumber("(611)67-890").getName()).isEqualTo(patientDto.getName());
        assertThat(service.getByPhoneNumber("(611)67-890").getId()).isNotNull();
        assertThat(service.getByPhoneNumber("(611)67-890").getId()).isNotEmpty();
    }

    @Test
    void itShouldDeletePatient() throws Exception {
        //Given
        var id = service.getByPhoneNumber("(611)67-890").getId();

        //When
        var result = mockMvc.perform(delete(String.format("http://localhost:8080/api/v1/patients/delete/%s", id)));

        //Then
        result.andExpect(status().isOk());
        assertThat(service.getById(id)).isNull();
    }
}
