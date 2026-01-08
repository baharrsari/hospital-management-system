package com.hospital.model;

/**
 * Represents an appointment record.
 */
public class Appointment {

    private Integer id;                 // unique appointment identifier (primary key)
    private Integer patientId;           // referenced patient id (foreign key)
    private Integer doctorId;            // referenced doctor id (foreign key)
    private String appointmentDate;      // appointment date in yyyy-MM-dd format
    private String appointmentTime;      // appointment time in HH:mm:ss format
    private String notes;                // optional notes about the appointment

    public Appointment() {               // default constructor
    }

    public Appointment(
            Integer id,                  // appointment id
            Integer patientId,            // patient id
            Integer doctorId,             // doctor id
            String appointmentDate,       // appointment date
            String appointmentTime,       // appointment time
            String notes                  // additional notes
    ) {
        this.id = id;                     // assign appointment id
        this.patientId = patientId;       // assign patient id
        this.doctorId = doctorId;         // assign doctor id
        this.appointmentDate = appointmentDate; // assign appointment date
        this.appointmentTime = appointmentTime; // assign appointment time
        this.notes = notes;               // assign notes
    }

    public Integer getId() {              // returns appointment id
        return id;
    }

    public void setId(Integer id) {       // sets appointment id
        this.id = id;
    }

    public Integer getPatientId() {        // returns patient id
        return patientId;
    }

    public void setPatientId(Integer patientId) { // sets patient id
        this.patientId = patientId;
    }

    public Integer getDoctorId() {         // returns doctor id
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) { // sets doctor id
        this.doctorId = doctorId;
    }

    public String getAppointmentDate() {   // returns appointment date
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) { // sets appointment date
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {   // returns appointment time
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) { // sets appointment time
        this.appointmentTime = appointmentTime;
    }

    public String getNotes() {              // returns appointment notes
        return notes;
    }

    public void setNotes(String notes) {    // sets appointment notes
        this.notes = notes;
    }
}
