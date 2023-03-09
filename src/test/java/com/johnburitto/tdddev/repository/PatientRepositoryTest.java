package com.johnburitto.tdddev.repository;

import com.johnburitto.tdddev.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataMongoTest
class PatientRepositoryTest {
    @Autowired
    PatientRepository underTest;

    @BeforeEach
    void setUp() {
        var john = new Patient("John", "(567)8090", "john@gmail.com");
        var freddie = new Patient("Freddie", "(100)4000", "freddie@gmail.com");

        john.setCreatedAt(LocalDateTime.now().minusDays(5));
        freddie.setCreatedAt(LocalDateTime.now().minusDays(7));

        underTest.saveAll(List.of(john, freddie));
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckTheCollectionIsNotEmpty() {
        assertFalse(underTest.findAll().isEmpty());
        assertEquals(2, underTest.findAll().size());
    }

    @Test
    void itShouldSavePatient() {
        //Given
        var paul = new Patient("Paul", "(567)8090", "paul@gmail.com");

        paul.setCreatedAt(LocalDateTime.now().minusDays(3));

        //When
        underTest.save(paul);

        //Then
        Patient forTest = underTest.findAll()
                .stream()
                .filter(patient -> patient.getName().equals("Paul"))
                .findFirst()
                .orElse(null);

        assertNotNull(forTest);
        assertNotNull(forTest.getId());
        assertFalse(forTest.getId().isEmpty());
        assertEquals(paul.getName(), forTest.getName());
    }

    @Test
    void itShouldFindPatientsCreatedAfter() {
        //Given
        int numberOfDaysBefore = 6;

        //When
        var patients = underTest.findAllByCreatedAtAfter(LocalDateTime.now().minusDays(numberOfDaysBefore));

        //Then
        assertNotNull(patients);
        assertEquals(1, patients.size());
    }

    @Test
    void itShouldUpdatePatient() {
        //Given
        Patient johnLennon = underTest.findAll()
                .stream()
                .filter(patient -> patient.getName().equals("John"))
                .findFirst()
                .orElse(null);
        String johnLennonId = Objects.requireNonNull(johnLennon).getId();

        Objects.requireNonNull(johnLennon).setName("John Lennon");

        //When
        underTest.save(johnLennon);

        //Then
        Patient forTest = underTest.findById(johnLennonId).orElse(null);

        assertNotNull(forTest);
        assertEquals("John Lennon", forTest.getName());
    }

    @Test
    void itShouldDeletePatient() {
        //Given
        Patient john = underTest.findAll()
                .stream()
                .filter(patient -> patient.getName().equals("John"))
                .findFirst()
                .orElse(null);
        String johnId = Objects.requireNonNull(john).getId();

        //When
        underTest.deleteById(johnId);

        //Then
        assertFalse(underTest.findAll().contains(john));
    }

    @Test
    void itShouldFindPatientsByPhoneCityCode() {
        //Give
        String cityCode = "(100)";

        //When
        Patient forTest = underTest.findAllByPhoneNumberContains(cityCode).stream().findFirst().orElse(null);

        //Then
        assertNotNull(forTest);
        assertEquals("Freddie", forTest.getName());
    }

    @Test
    void itShouldFindPatientsByNameAndEmail() {
        //Given
        String name = "John";
        String emailPart = "john@gmail.com";

        //When
        Patient forTest = underTest.findAllByNameAndEmailContains(name, emailPart).stream().findFirst().orElse(null);

        //Then
        assertNotNull(forTest);
        assertEquals("John", forTest.getName());
        assertEquals("john@gmail.com", forTest.getEmail());
    }

    @Test
    void itShouldFindPatientsByNameAndWithNameInLowerCaseInEmail() {
        //Given
        String name = "John";
        String emailPart = name.toLowerCase();

        //When
        Patient forTest = underTest.findAllByNameAndEmailContains(name, emailPart).stream().findFirst().orElse(null);

        //Then
        assertNotNull(forTest);
        assertEquals("John", forTest.getName());
        assertEquals("john@gmail.com", forTest.getEmail());
    }

    @Test
    void itShouldCheckThatPatientAlreadyExist() {
        //Given
        String phoneNumber = "(567)8090";

        //When
        var result = underTest.existsPatientByPhoneNumber(phoneNumber);

        //Then
        assertTrue(result);
    }
}