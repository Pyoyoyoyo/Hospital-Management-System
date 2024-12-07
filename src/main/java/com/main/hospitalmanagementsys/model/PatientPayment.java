package com.main.hospitalmanagementsys.model;

import javafx.beans.property.SimpleStringProperty;

public class PatientPayment {
    private String patientName;
    private String doctorName;
    private String paymentStatus;

    public PatientPayment(String patientName, String paymentStatus) {
        this.patientName = patientName;
        this.paymentStatus = paymentStatus;
    }

    public PatientPayment(String patientName, String doctorName, String paymentStatus) {
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.paymentStatus = paymentStatus;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
