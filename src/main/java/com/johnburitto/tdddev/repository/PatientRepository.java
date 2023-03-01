package com.johnburitto.tdddev.repository;

import com.johnburitto.tdddev.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    List<Patient> findAllByCreatedAtAfter(LocalDateTime date);
    List<Patient> findAllByPhoneNumberContains(String cityCode);
    List<Patient> findAllByNameAndEmailContains(String name, String emailPart);
}
