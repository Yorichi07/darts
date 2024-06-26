package com.darts.server.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient_details")
public class Patient_details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Integer patient_id;

    @Column(name = "date_of_birth")
    private Date date_of_birth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "medical_conditions")
    private String medical_conditions;

    @Column(name = "medications")
    private String medications;

    @Column(name = "allergies")
    private String allergies;

    @Column(name = "last_appointment_date")
    private Date last_appointment_date;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "emer_name")
    private String emer_Name;

    @Column(name = "emer_rel")
    private String emer_Rel;

    @Column(name = "emer_phn")
    private String emer_Phn;

    @OneToOne(mappedBy = "patient_details")
    private Users users;

    public Integer getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Integer patient_id) {
        this.patient_id = patient_id;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMedical_conditions() {
        return medical_conditions;
    }

    public void setMedical_conditions(String medical_conditions) {
        this.medical_conditions = medical_conditions;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public Date getLast_appointment_date() {
        return last_appointment_date;
    }

    public void setLast_appointment_date(Date last_appointment_date) {
        this.last_appointment_date = last_appointment_date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public void setEmer_Name(String name){
        this.emer_Name = name;
    }

    public void setEmer_Rel(String Rel){
        this.emer_Rel = Rel;
    }

    public void setEmer_Phn(String Phn){
        this.emer_Phn = Phn;
    }

    public String getEmer_name(){
        return this.emer_Name;
    }

    public String getEmer_Rel(){
        return this.emer_Rel;
    }

    public String getEmer_Phn(){
        return this.emer_Phn;
    }
}