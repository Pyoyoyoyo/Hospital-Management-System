package com.main.hospitalmanagementsys.model;

public class PatientPayment {
    private String patientName;
    private String paymentStatus;

    public PatientPayment(String patientName, String paymentStatus) {
        this.patientName = patientName;
        this.paymentStatus = paymentStatus;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
