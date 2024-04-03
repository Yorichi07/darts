package com.darts.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "hospital")
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FID")
    private Long FID;

    @Column(name = "NAME")
    private String NAME;

    @Column(name = "ADDRESS")
    private String ADDRESS;

    @Column(name = "CITY")
    private String CITY;

    @Column(name = "STATE")
    private String STATE;

    @Column(name = "ZIP")
    private long ZIP;

    @Column(name = "TELEPHONE")
    private String TELEPHONE;

    @Column(name = "TYPE")
    private String TYPE;

    @Column(name = "STATUS")
    private String STATUS;

    @Column(name = "POPULATION")
    private long POPULATION;

    @Column(name = "COUNTY")
    private String COUNTY;

    @Column(name = "COUNTRY")
    private String COUNTRY;

    @Column(name = "LATITUDE")
    private double LATITUDE;
    
    @Column(name = "LONGITUDE")
    private double LONGITUDE;

    @Column(name = "WEBSITE")
    private String WEBSITE;

    @Column(name = "OWNER")
    private String OWNER;

    @Column(name = "BEDS")
    private long BEDS;

    public Long getFID() {
        return FID;
    }

    public void setFID(Long FID) {
        this.FID = FID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public long getZIP() {
        return ZIP;
    }

    public void setZIP(long ZIP) {
        this.ZIP = ZIP;
    }

    public String getTELEPHONE() {
        return TELEPHONE;
    }

    public void setTELEPHONE(String TELEPHONE) {
        this.TELEPHONE = TELEPHONE;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public long getPOPULATION() {
        return POPULATION;
    }

    public void setPOPULATION(long POPULATION) {
        this.POPULATION = POPULATION;
    }

    public String getCOUNTY() {
        return COUNTY;
    }

    public void setCOUNTY(String COUNTY) {
        this.COUNTY = COUNTY;
    }

    public String getCOUNTRY() {
        return COUNTRY;
    }

    public void setCOUNTRY(String COUNTRY) {
        this.COUNTRY = COUNTRY;
    }

    public double getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(double LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public double getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(double LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getWEBSITE() {
        return WEBSITE;
    }

    public void setWEBSITE(String WEBSITE) {
        this.WEBSITE = WEBSITE;
    }

    public String getOWNER() {
        return OWNER;
    }

    public void setOWNER(String OWNER) {
        this.OWNER = OWNER;
    }

    public long getBEDS() {
        return BEDS;
    }

    public void setBEDS(long BEDS) {
        this.BEDS = BEDS;
    }
}
