package com.hospital.model;

/**
 * Represents a doctor record.
 */
public class Doctor {

    private Integer id;               // unique identifier (primary key)
    private String firstName;          // doctor's first name
    private String lastName;           // doctor's last name
    private String specialization;     // medical specialization / department
    private String phone;              // contact phone number

    public Doctor() { }                // default constructor

    public Doctor(
            Integer id,                // doctor id
            String firstName,           // first name
            String lastName,            // last name
            String specialization,      // specialization (e.g. Cardiology)
            String phone                // phone number
    ) {
        this.id = id;                  // assign id
        this.firstName = firstName;    // assign first name
        this.lastName = lastName;      // assign last name
        this.specialization = specialization; // assign specialization
        this.phone = phone;            // assign phone
    }

    public Integer getId() {            // returns doctor id
        return id;
    }

    public void setId(Integer id) {     // sets doctor id
        this.id = id;
    }

    public String getFirstName() {      // returns first name
        return firstName;
    }

    public void setFirstName(String firstName) { // sets first name
        this.firstName = firstName;
    }

    public String getLastName() {       // returns last name
        return lastName;
    }

    public void setLastName(String lastName) {   // sets last name
        this.lastName = lastName;
    }

    public String getSpecialization() { // returns specialization
        return specialization;
    }

    public void setSpecialization(String specialization) { // sets specialization
        this.specialization = specialization;
    }

    public String getPhone() {          // returns phone number
        return phone;
    }

    public void setPhone(String phone) { // sets phone number
        this.phone = phone;
    }
}
