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

import java.util.List;
import java.util.Optional;

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
    void itNotShouldSavePatientWhenPhoneIsExistAndExceptionThrow() {
        //Given
        String phoneNumber = "000";
        String email = "example@example.com";
        var patientDto = new PatientDto("Some name", phoneNumber, email);
        Patient john = new Patient("John", phoneNumber, email);

        given(mockRepository.existsPatientByPhoneNumber(phoneNumber)).willReturn(true);
        given(mockRepository.findPatientByPhoneNumber(phoneNumber)).willReturn(Optional.of(john));

        //When
        assertThatThrownBy(() -> underTest.create(patientDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("The number %s is already taken", patientDto.getPhoneNumber()));

        //Then
        verify(mockRepository, never()).save(any(Patient.class));
    }

    @Test
    void itNotShouldSavePatientWhenPhoneIsNotValid() {
        //Given
        String phoneNumber = "0";
        String email = "example@example.com";
        var patientDto = new PatientDto("Some name", phoneNumber, email);

        //When
        assertThatThrownBy(() -> underTest.create(patientDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("The number %s isn't valid", patientDto.getPhoneNumber()));

        //Then
        verify(mockRepository, never()).save(any(Patient.class));
    }

    @Test
    void itNotShouldSavePatientWhenPhoneAndNameIsExistAndExceptionThrow() {
        //Given
        String phoneNumber = "000";
        String email = "example@example.com";
        var patientDto = new PatientDto("John", phoneNumber, email);
        Patient john = new Patient("John", phoneNumber, email);

        given(mockRepository.existsPatientByPhoneNumber(phoneNumber)).willReturn(true);
        given(mockRepository.findPatientByPhoneNumber(phoneNumber)).willReturn(Optional.of(john));

        //When
        assertThatThrownBy(() -> underTest.create(patientDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("This patient is already registered");

        //Then
        verify(mockRepository, never()).save(any(Patient.class));
    }

    @Test
    void itShouldGetAllPatients() {
        //Given
        List<Patient> patients = List.of(
                new Patient("1", "John", "911", "john@gmail.com"),
                new Patient("2", "Freddie", "922", "freddie@gmail.com")
        );

        given(mockRepository.findAll()).willReturn(patients);

        //When
        var result = underTest.getAll();

        //Then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(patients);
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void itShouldGetPatientById() {
        //Given
        Patient john = new Patient("1", "John", "911", "john@gmail.com");

        given(mockRepository.findById("1")).willReturn(Optional.of(john));

        //When
        var result = underTest.getById("1");

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("John");
        assertThat(result.getPhoneNumber()).isEqualTo("911");
        assertThat(result.getEmail()).isEqualTo("john@gmail.com");
    }

    @Test
    void itShouldThrowExceptionIfPatientDoesntExist() {
        //Given
        String id = "2";

        given(mockRepository.findById(id)).willReturn(Optional.empty());

        //When
        assertThatThrownBy(() -> underTest.getById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(String.format("Not found with id = %s", id));

        //Then
    }

    @Test
    void itShouldUpdatePatient() {
        //Given
        var oldJohn = new Patient("1", "John", "911", "oldJohn@gmail.com");
        var patientDto = new PatientDto("John", "911", "john@gmail.com");

        given(mockRepository.findById("1")).willReturn(Optional.of(oldJohn));

        //When
        underTest.update("1", patientDto);

        //Then
        then(mockRepository).should().save(argumentCaptor.capture());

        var updatedPatient = argumentCaptor.getValue();

        assertThat(updatedPatient).isNotNull();
        assertThat(updatedPatient.getName()).isEqualTo("John");
        assertThat(updatedPatient.getPhoneNumber()).isEqualTo("911");
        assertThat(updatedPatient.getEmail()).isEqualTo("john@gmail.com");

        verify(mockRepository).save(any(Patient.class));
        verify(mockRepository, times(1)).save(updatedPatient);
    }

    @Test
    void itShouldDeletePatient() {
        //Given
        var id = "1";

        //When
        underTest.delete(id);

        //Then
        verify(mockRepository, times(1)).deleteById(id);
    }

    @Test
    void itThrowExceptionWhenPatientDoesntInDatabase() {
        //Given
        var id = "2";

        //When
        assertThatThrownBy(() -> underTest.delete(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(String.format("Not found with id = %s", id));

        //Then
    }
}