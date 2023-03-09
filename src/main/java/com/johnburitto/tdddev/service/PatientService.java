package com.johnburitto.tdddev.service;

import com.johnburitto.tdddev.dto.PatientDto;
import com.johnburitto.tdddev.model.Patient;
import com.johnburitto.tdddev.repository.PatientRepository;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService implements IService<Patient, PatientDto> {
    private final PatientRepository repository;
    private final DozerBeanMapper mapper;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
        mapper = new DozerBeanMapper();
    }

    @Override
    public List<Patient> getAll() {
        return repository.findAll();
    }

    @Override
    public Patient getById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Patient create(PatientDto patientDto) {
        if (repository.existsPatientByPhoneNumber(patientDto.getPhoneNumber())) {
            return null;
        }

        var patient = new Patient();

        mapper.map(patientDto, patient);

        return repository.save(patient);
    }

    @Override
    public Patient update(String id, PatientDto patientDto) {
        var patient = getById(id);

        mapper.map(patientDto, patient);

        return repository.save(patient);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}
