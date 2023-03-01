package com.johnburitto.tdddev.controller;

import com.johnburitto.tdddev.dto.PatientDto;
import com.johnburitto.tdddev.model.Patient;
import com.johnburitto.tdddev.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {
    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Patient> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Patient getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping("/create")
    public Patient create(@RequestBody PatientDto patientDto) {
        return service.create(patientDto);
    }

    @PutMapping("/update/{id}")
    public Patient update(@PathVariable String id, @RequestBody PatientDto patientDto) {
        return service.update(id, patientDto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
