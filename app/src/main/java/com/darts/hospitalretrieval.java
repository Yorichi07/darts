package com.darts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class hospitalretrieval {
    private static final String url = "jdbc:mysql://localhost:3306/medical_records?characterEncoding=utf8";
    private static final String username = "root";
    private static final String password = "root";

    public void getHospital(Scanner scanner) {
        System.out.println("Enter the hospital ID:");
        int hospitalId = scanner.nextInt();

        Hospital hospital = getHospitalDetails(hospitalId);

        if (hospital != null) {
            System.out.println("\n");
            System.out.println("Hospital ID:" + hospital.getFID());
            System.out.println("Hospital Name:" + hospital.getNAME());
            System.out.println("Hospital ADDRESS:" + hospital.getADDRESS());
            System.out.println("Hospital CITY:" + hospital.getcity());
            System.out.println("Hospital STATE:" + hospital.getstate());
            System.out.println("Hospital ZIP:" + hospital.getzip());
            System.out.println("Hospital TELEPHONE:" + hospital.gettelephone());
            System.out.println("Hospital TYPE:" + hospital.gettype());
            System.out.println("Hospital STATUS:" + hospital.getstatus());
            System.out.println("Hospital POPULATION:" + hospital.getpopulation());
            System.out.println("Hospital COUNTY:" + hospital.getcounty());
            System.out.println("Hospital COUNTRY:" + hospital.getcountry());
            System.out.println("Hospital LATITUDE:" + hospital.getlatitude());
            System.out.println("Hospital LONGITUDE:" + hospital.getlongitude());
            System.out.println("Hospital OWNER:" + hospital.getowner());
            System.out.println("Hospital BEDS:");
            try {
                System.out.println(hospital.getbeds()); // Assuming beds are integers
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid value for beds. Setting to 0.");
                hospital.setBEDS(0); // Set to a default value in case of parsing error
            }
            System.out.println("\n");
            System.out.println("\n");
        } else {
            System.out.println("Hospital not found");
        }

        getNearestHospitals(hospitalId);
    }

    public Hospital getHospitalDetails(int hospitalId) {
        Hospital hospital = null;

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM hospital WHERE FID = ?";        //will replace ? with values later using prepared statements of SQL
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, hospitalId);     //set the value of the 1st ? in the query with provided patientId
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) { // Check if ResultSet has any rows
                    hospital = new Hospital();
                    hospital.setFID(rs.getInt("FID"));
                    hospital.setNAME(rs.getString("NAME"));
                    hospital.setADDRESS(rs.getString("ADDRESS"));
                    hospital.setCITY(rs.getString("CITY"));
                    hospital.setSTATE(rs.getString("STATE"));
                    hospital.setZIP(rs.getInt("ZIP"));
                    hospital.setTELEPHONE(rs.getString("TELEPHONE"));
                    hospital.setTYPE(rs.getString("TYPE"));
                    hospital.setSTATUS(rs.getString("STATUS"));
                    hospital.setPOPULATION(rs.getInt("POPULATION"));
                    hospital.setCOUNTY(rs.getString("COUNTY"));
                    hospital.setCOUNTRY(rs.getString("COUNTRY"));
                    hospital.setLATITUDE(rs.getDouble("LATITUDE"));
                    hospital.setLONGITUDE(rs.getDouble("LONGITUDE"));
                    hospital.setOWNER(rs.getString("OWNER"));
                    String bedsStr = rs.getString("BEDS");
                    if (bedsStr.equals("NOT AVAILABLE")) {
                        hospital.setBEDS(0); // or set it to a default value
                    } else {
                        hospital.setBEDS(Integer.parseInt(bedsStr));
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hospital;
    }


    public static void getNearestHospitals(int hospitalId) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT h.*, (6371 * ACOS(COS(RADIANS(h1.LATITUDE)) * COS(RADIANS(h.LATITUDE)) * COS(RADIANS(h1.LONGITUDE - h.LONGITUDE)) + SIN(RADIANS(h1.LATITUDE)) * SIN(RADIANS(h.LATITUDE)))) AS distance " +
                    "FROM hospital h " +
                    "CROSS JOIN (SELECT LATITUDE, LONGITUDE FROM hospital WHERE FID = ?) h1 " +
                    "WHERE h.FID <> ? " +
                    "ORDER BY distance " +
                    "LIMIT 5"; // Fixed limit of 5 nearest hospitals
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, hospitalId); // Hospital ID for which we want to find nearest hospitals
                pstmt.setInt(2, hospitalId); // Exclude the hospital itself from the results
                ResultSet rs = pstmt.executeQuery();
                int count = 0;
                System.out.println("Nearest Hospitals to this hospitals are: \n");
                while (rs.next()) {
                    count++;
                    System.out.println("\n");
                    System.out.println("Hospital " + count + ":");
                    System.out.println("Name: " + rs.getString("NAME"));
                    System.out.println("Hospital ID: " + rs.getInt("FID"));
                    System.out.println("Beds: " + rs.getInt("BEDS"));
                    System.out.println("Latitude: " + rs.getDouble("LATITUDE"));
                    System.out.println("Latitude: " + rs.getDouble("LONGITUDE"));
                }
                if (count == 0) {
                    System.out.println("No nearest hospitals found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

class Hospital {
    private int FID;
    private String NAME;
    private String ADDRESS;
    private String CITY;
    private String STATE;
    private int ZIP;
    private String TELEPHONE;
    private String TYPE;
    private String STATUS;
    private int POPULATION;
    private String COUNTY;
    private String COUNTRY;
    private double LATITUDE;
    private double LONGITUDE;
    private String OWNER;
    private int BEDS;

    public int getFID() {
        return FID;
    }

    public String getNAME() {
        return NAME;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public String getcity() {
        return CITY;
    }

    public String getstate() {
        return STATE;
    }

    public int getzip() {
        return ZIP;
    }

    public String gettelephone() {
        return TELEPHONE;
    }

    public String gettype() {
        return TYPE;
    }

    public String getstatus() {
        return STATUS;
    }

    public int getpopulation() {
        return POPULATION;
    }

    public String getcounty() {
        return COUNTY;
    }

    public String getcountry() {
        return COUNTRY;
    }

    public double getlatitude() {
        return LATITUDE;
    }

    public double getlongitude() {
        return LONGITUDE;
    }

    public String getowner() {
        return OWNER;
    }

    public int getbeds() {
        return BEDS;
    }

    public void setFID(int FID) {
        this.FID = FID;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public void setZIP(int ZIP) {
        this.ZIP = ZIP;
    }

    public void setTELEPHONE(String TELEPHONE) {
        this.TELEPHONE = TELEPHONE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public void setPOPULATION(int POPULATION) {
        this.POPULATION = POPULATION;
    }

    public void setCOUNTY(String COUNTY) {
        this.COUNTY = COUNTY;
    }

    public void setCOUNTRY(String COUNTRY) {
        this.COUNTRY = COUNTRY;
    }

    public void setLATITUDE(double LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public void setLONGITUDE(double LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public void setOWNER(String OWNER) {
        this.OWNER = OWNER;
    }

    public void setBEDS(int BEDS) {
        this.BEDS = BEDS;
    }

}
