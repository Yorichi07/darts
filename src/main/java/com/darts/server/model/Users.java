package com.darts.server.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UID")
    private Integer UID;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "qr_path")
    private String qrPath;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PID", referencedColumnName = "patient_id")
    private Patient_details patient_details;

    public Integer getUID() {
        return UID;
    }

    public void setUID(Integer UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Patient_details getPatient_details() {
        return patient_details;
    }

    public void setPatient_details(Patient_details patient_details) {
        this.patient_details = patient_details;
    }

    public String getQrPath(){
        return this.qrPath;
    }

    public void setQrPath(String qrPth){
        this.qrPath = qrPth;
    }
}
