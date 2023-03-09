package com.johnburitto.tdddev.service;

import com.johnburitto.tdddev.dto.PatientDto;
import com.johnburitto.tdddev.model.Patient;
import com.johnburitto.tdddev.repository.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class PatientServiceTest {
    @Captor
    private ArgumentCaptor<Patient> argumentCaptor;
    @Mock
    private PatientRepository mockRepository;
    private PatientService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new PatientService(mockRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void itShouldSavePatient() {
        //Given
        String phoneNumber = "000";
        String email = "example@example.com";
        var patientDto = new PatientDto("Some name", phoneNumber, email);

        given(mockRepository.existsPatientByPhoneNumber(phoneNumber)).willReturn(false);

        //When
        underTest.create(patientDto);

        //Then
        then(mockRepository).should().save(argumentCaptor.capture());

        var savedPatient = argumentCaptor.getValue();
        assertNotNull(savedPatient);
        assertThat(savedPatient.getName()).isEqualTo(patientDto.getName());
        assertThat(savedPatient.getPhoneNumber()).isEqualTo(patientDto.getPhoneNumber());
        assertThat(savedPatient.getEmail()).isEqualTo(patientDto.getEmail());

        verify(mockRepository).save(any(Patient.class));
        verify(mockRepository, times(1)).save(savedPatient);
    }

    @Test
    void itNotShouldSavePatientWhenPhoneIsExist() {
        //Given
        String phoneNumber = "000";
        String email = "example@example.com";
        var patientDto = new PatientDto("Some name", phoneNumber, email);

        given(mockRepository.existsPatientByPhoneNumber(phoneNumber)).willReturn(true);

        //When
        underTest.create(patientDto);

        //Then
        verify(mockRepository, never()).save(any(Patient.class));
    }
}