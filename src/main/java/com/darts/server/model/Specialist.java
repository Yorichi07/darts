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
    private Integer docID;

    @Column(name = "Doc_Name")
    private String docName;

    @Column(name = "Speciality")
    private String speciality;

    @Column(name="emergency")
    private String emergency;

    public Integer getDocID() {
        return docID;
    }

    public void setDocID(Integer docID) {
        this.docID = docID;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getEmergency(){
        return emergency;
    }

    public void setEmergency(String emergency){
        this.emergency=emergency;
    }
}
