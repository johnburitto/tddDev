package com.johnburitto.tdddev.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends AuditMetadata {
    @Id
    private String id;
    private String name;
    private String phoneNumber;
    private String eMail;

    public Patient(String name, String phoneNumber, String eMail) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return id.equals(patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
