package com.darts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HForm {
    public static void main(String[] args) {
        // Creating the hospital admission form fields
        Map<String, Object> admissionForm = new HashMap<>();
        
        // Patient Information
        Map<String, String> patientInfo = new HashMap<>();
        patientInfo.put("first_name", "");
        patientInfo.put("last_name", "");
        patientInfo.put("date_of_birth", "");
        patientInfo.put("gender", "");
        patientInfo.put("address", "");
        patientInfo.put("phone_number", "");
        patientInfo.put("email", "");
        admissionForm.put("patient_info", patientInfo);
        
        // Emergency Contact
        Map<String, String> emergencyContact = new HashMap<>();
        emergencyContact.put("name", "");
        emergencyContact.put("relationship", "");
        emergencyContact.put("phone_number", "");
        admissionForm.put("emergency_contact", emergencyContact);
        
        // // Insurance Information
        // Map<String, String> insuranceInfo = new HashMap<>();
        // insuranceInfo.put("insurance_company", "");
        // insuranceInfo.put("policy_number", "");
        // insuranceInfo.put("group_number", "");
        // insuranceInfo.put("primary_insured", "");
        // admissionForm.put("insurance_info", insuranceInfo);
        
        // // Medical History
        // Map<String, Object> medicalHistory = new HashMap<>();
        // medicalHistory.put("allergies", new ArrayList<String>());
        // medicalHistory.put("current_medications", new ArrayList<String>());
        // medicalHistory.put("past_medical_history", "");
        // medicalHistory.put("family_medical_history", "");
        // admissionForm.put("medical_history", medicalHistory);
        
        // Other Information
        admissionForm.put("reason_for_admission", "");
        
        // Map<String, String> primaryPhysician = new HashMap<>();
        // primaryPhysician.put("name", "");
        // primaryPhysician.put("phone_number", "");
        // admissionForm.put("primary_physician", primaryPhysician);
        
        admissionForm.put("preferred_language", "");
        admissionForm.put("signature", "");
        admissionForm.put("date_of_admission", "");

        // Displaying the admission form
        System.out.println("Hospital Admission Form");
        System.out.println("-----------------------");
        displayForm(admissionForm, "");
    }

    // Function to recursively display the -form fields
    @SuppressWarnings("unchecked")
    private static void displayForm(Map<String, Object> form, String indent) {
        for (Map.Entry<String, Object> entry : form.entrySet()) {
            System.out.print(indent + entry.getKey() + ": ");
            if (entry.getValue() instanceof Map) {
                System.out.println();
                displayForm((Map<String, Object>) entry.getValue(), indent + "  ");
            } else if (entry.getValue() instanceof List) {
                System.out.println();
                for (String item : (List<String>) entry.getValue()) {
                    System.out.println(indent + "  - " + item);
                }
            } else {
                System.out.println(entry.getValue());
            }
        }
    }
}