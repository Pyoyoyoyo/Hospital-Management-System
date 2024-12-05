package com.main.hospitalmanagementsys.model;

public class Patient {
    private String patientName;
    private String medicalHistory;
    private String insuranceInformation;
    private String registrationNumber;
    private String contactNumber;
    private String address;
    private String email;

    public Patient(String patientName, String medicalHistory, String insuranceInformation, String registrationNumber,
                   String contactNumber, String address, String email) {
        this.patientName = patientName;
        this.medicalHistory = medicalHistory;
        this.insuranceInformation = insuranceInformation;
        this.registrationNumber = registrationNumber;
        this.contactNumber = contactNumber;
        this.address = address;
        this.email = email;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getInsuranceInformation() {
        return insuranceInformation;
    }

    public void setInsuranceInformation(String insuranceInformation) {
        this.insuranceInformation = insuranceInformation;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
