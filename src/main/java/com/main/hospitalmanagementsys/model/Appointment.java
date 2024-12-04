package com.main.hospitalmanagementsys.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Appointment {
    private String time;
    private LocalDate date;
    private String patientName;
    private String doctorName;

    public Appointment(String time, LocalDate date, String patientName, String doctorName) {
        this.time = time;
        this.date = date;
        this.patientName = patientName;
        this.doctorName = doctorName;
    }

    // Getters and Setters
    public String getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    // JavaFX Property Bindings (for TableView)
    public StringProperty timeProperty() {
        return new SimpleStringProperty(time);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return new SimpleObjectProperty<>(date);
    }

    public StringProperty patientNameProperty() {
        return new SimpleStringProperty(patientName);
    }

    public StringProperty doctorNameProperty() {
        return new SimpleStringProperty(doctorName);
    }
}
