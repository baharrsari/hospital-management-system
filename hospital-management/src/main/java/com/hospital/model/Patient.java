package com.hospital.model; // model package

import java.time.LocalDate; // date type

/**
 * Represents a patient record in the system.
 */
public class Patient { // patient model class

    private Integer id; // unique identifier (null when not saved yet)
    private String firstName; // patient's first name
    private String lastName; // patient's last name
    private String phone; // phone number
    private String gender; // gender
    private LocalDate birthDate; // birth date (can be null)

    public Patient() { } // no-arg constructor

    public Patient(Integer id, String firstName, String lastName, String phone, String gender, LocalDate birthDate) { // full constructor
        this.id = id; // set ID
        this.firstName = firstName; // set first name
        this.lastName = lastName; // set last name
        this.phone = phone; // set phone
        this.gender = gender; // set gender
        this.birthDate = birthDate; // set birth date
    }

    public Integer getId() { return id; } // get ID
    public void setId(Integer id) { this.id = id; } // set ID

    public String getFirstName() { return firstName; } // get first name
    public void setFirstName(String firstName) { this.firstName = firstName; } // set first name

    public String getLastName() { return lastName; } // get last name
    public void setLastName(String lastName) { this.lastName = lastName; } // set last name

    public String getPhone() { return phone; } // get phone
    public void setPhone(String phone) { this.phone = phone; } // set phone

    public String getGender() { return gender; } // get gender
    public void setGender(String gender) { this.gender = gender; } // set gender

    public LocalDate getBirthDate() { return birthDate; } // get birth date
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; } // set birth date
}
