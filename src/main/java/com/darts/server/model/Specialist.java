package com.darts.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "specialist")
public class Specialist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Doc_ID")
    private Long Doc_ID;

    @Column(name = "Doc_Name")
    private String Doc_Name;

    @Column(name = "Speciality")
    private String Speciality;

    public Long getDoc_ID() {
        return Doc_ID;
    }

    public void setDoc_ID(Long Doc_ID) {
        this.Doc_ID = Doc_ID;
    }

    public String getDoc_Name() {
        return Doc_Name;
    }

    public void setDoc_Name(String Doc_Name) {
        this.Doc_Name = Doc_Name;
    }

    public String getSpeciality() {
        return Speciality;
    }

    public void setSpeciality(String Speciality) {
        this.Speciality = Speciality;
    }
}
