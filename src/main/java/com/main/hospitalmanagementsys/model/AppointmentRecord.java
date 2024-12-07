package com.main.hospitalmanagementsys.model;

import java.time.LocalDate;

public class AppointmentRecord {
    private int id;
    private int appointmentCode;
    private LocalDate date;
    private String time;
    private String status;
    private int doctorId;
    private int patientId;
    private String patientName;
    private String doctorName;

    public AppointmentRecord(int id, int appointmentCode, LocalDate date, String time, String status, int doctorId, int patientId) {
        this.id = id;
        this.appointmentCode = appointmentCode;
        this.date = date;
        this.time = time;
        this.status = status;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

    public AppointmentRecord(int id, int appointmentCode, LocalDate date, String time, String status, int doctorId, int patientId, String patientName, String doctorName) {
        this.id = id;
        this.appointmentCode = appointmentCode;
        this.date = date;
        this.time = time;
        this.status = status;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorName = doctorName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppointmentCode() {
        return appointmentCode;
    }

    public void setAppointmentCode(int appointmentCode) {
        this.appointmentCode = appointmentCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
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

    // toString method for debugging
    @Override
    public String toString() {
        return "AppointmentRecord{" +
                "id=" + id +
                ", appointmentCode=" + appointmentCode +
                ", date=" + date +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                ", doctorId=" + doctorId +
                ", patientId=" + patientId +
                '}';
    }
}
