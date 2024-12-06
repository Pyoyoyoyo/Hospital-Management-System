package com.main.hospitalmanagementsys.model;

import java.time.LocalDate;

public class DetailedAppointment {
    private String time;
    private LocalDate date;
    private String patientName;
    private String doctorName;
    private String paymentStatus;

    // Constructor
    public DetailedAppointment(String time, LocalDate date, String patientName,
                               String doctorName, String paymentStatus) {
        this.time = time;
        this.date = date;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public String getTime() { return time; }
    public LocalDate getDate() { return date; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getPaymentStatus() { return paymentStatus; }
}

