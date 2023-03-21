package com.johnburitto.tdddev.repository;

import com.johnburitto.tdddev.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    boolean existsPatientByPhoneNumber(String phoneNumber);
    Optional<Patient> findPatientByPhoneNumber(String phoneNumber);
}
