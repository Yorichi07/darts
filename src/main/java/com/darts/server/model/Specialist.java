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

    @Column(name = "qrPath")
    private String qrPath;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    public String password;

    public String getQrPath(){
        return this.qrPath;
    }

    public void setQrPath(String qr){
        this.qrPath = qr;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String usrnm){
        this.username = usrnm;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String paswrd){
        this.password = paswrd;
    }

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
}
