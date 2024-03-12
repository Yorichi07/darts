import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Date;

public class patientDetailsRetrieval {
    private static final String url = "jdbc:mysql://localhost:3306/medical_records";
    private static final String username = "root";
    private static final String password = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();

        scanner.close();

        Patient patient = getPatientDetails(patientId); 
        if (patient != null) {
            System.out.println("Patient ID: " + patient.getPatientId());
            System.out.println("Name: " + patient.getName());
            System.out.println("Date of Birth: " + patient.getDateOfBirth());
            System.out.println("Gender: " + patient.getGender());
            System.out.println("Medical Conditions: " + patient.getMedicalConditions());
            System.out.println("Medications: " + patient.getMedications());
            System.out.println("Allergies: " + patient.getAllergies());
            System.out.println("Last Appointment Date: " + patient.getLastAppointmentDate());
        } else {
            System.out.println("Patient not found.");
        }
    }

    public static Patient getPatientDetails(int patientId) {
        Patient patient = null;     //initializing patient object with null
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM patient_details WHERE patient_id = ?";        //will replace ? with values later using prepared statements of SQL
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, patientId);     //set the value of the 1st ? in the query with provided patientId
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        patient = new Patient();
                        patient.setPatientId(rs.getInt("patient_id"));
                        patient.setName(rs.getString("name"));
                        patient.setDateOfBirth(rs.getDate("date_of_birth"));
                        patient.setGender(rs.getString("gender"));
                        patient.setMedicalConditions(rs.getString("medical_conditions"));
                        patient.setMedications(rs.getString("medications"));
                        patient.setAllergies(rs.getString("allergies"));
                        patient.setLastAppointmentDate(rs.getDate("last_appointment_date"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }
}

class Patient {
    private int patientId;
    private String name;
    private Date dateOfBirth;
    private String gender;
    private String medicalConditions;
    private String medications;
    private String allergies;
    private Date lastAppointmentDate;

    // Getters and setters
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    } 

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    } 

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
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

    public Date getLastAppointmentDate() {
        return lastAppointmentDate;
    } 

    public void setLastAppointmentDate(Date lastAppointmentDate) {
        this.lastAppointmentDate = lastAppointmentDate;
    } 
}