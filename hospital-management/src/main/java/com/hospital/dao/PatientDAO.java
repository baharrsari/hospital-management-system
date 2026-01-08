package com.hospital.dao;

import java.sql.Connection;          // JDBC connection interface
import java.sql.Date;                // SQL Date type
import java.sql.PreparedStatement;   // precompiled SQL statement
import java.sql.ResultSet;           // result of SQL query
import java.sql.SQLException;        // SQL exception handling
import java.sql.Statement;           // SQL statement interface
import java.sql.Types;               // SQL type constants
import java.time.LocalDate;          // modern date API
import java.util.ArrayList;          // resizable list implementation
import java.util.List;               // list interface

import com.hospital.db.DBConnection; // database connection utility
import com.hospital.model.Patient;  // patient domain model

public class PatientDAO {

    public void create(Patient patient) throws SQLException { // inserts a new patient record
        String sql = """
                INSERT INTO patients (first_name, last_name, phone, gender, birth_date)
                VALUES (?, ?, ?, ?, ?)
                """; // SQL insert query

        try (Connection conn = DBConnection.getConnection(); // open DB connection
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // prepare statement

            ps.setString(1, patient.getFirstName()); // bind first name
            ps.setString(2, patient.getLastName());  // bind last name
            ps.setString(3, patient.getPhone());     // bind phone number
            ps.setString(4, patient.getGender());    // bind gender

            if (patient.getBirthDate() != null) {    // check birth date existence
                ps.setDate(5, Date.valueOf(patient.getBirthDate())); // bind birth date
            } else {
                ps.setNull(5, Types.DATE);            // set NULL if birth date is missing
            }

            ps.executeUpdate();                        // execute insert query

            try (ResultSet rs = ps.getGeneratedKeys()) { // fetch auto-generated keys
                if (rs.next()) patient.setId(rs.getInt(1)); // set generated patient id
            }
        }
    }

    public List<Patient> listAll() throws SQLException { // retrieves all patients
        String sql = """
                SELECT id, first_name, last_name, phone, gender, birth_date
                FROM patients
                ORDER BY id DESC
                """; // SQL select query

        List<Patient> result = new ArrayList<>(); // list to store patients

        try (Connection conn = DBConnection.getConnection(); // open DB connection
             PreparedStatement ps = conn.prepareStatement(sql); // prepare statement
             ResultSet rs = ps.executeQuery()) {               // execute query

            while (rs.next()) {                                // iterate over result set
                Integer id = rs.getInt("id");                  // read patient id
                String firstName = rs.getString("first_name"); // read first name
                String lastName = rs.getString("last_name");   // read last name
                String phone = rs.getString("phone");          // read phone
                String gender = rs.getString("gender");        // read gender

                Date bd = rs.getDate("birth_date");            // read birth date
                LocalDate birthDate = (bd != null) ? bd.toLocalDate() : null; // convert to LocalDate

                result.add(new Patient(                         // create Patient object
                        id,
                        firstName,
                        lastName,
                        phone,
                        gender,
                        birthDate
                ));
            }
        }

        return result; // return patient list
    }

    /**
     * Deletes patients by ids.
     * NOTE: If appointments reference patients, delete those appointments first.
     */
    public int deleteByIds(List<Integer> ids) throws SQLException { // deletes patients by id list
        if (ids == null || ids.isEmpty()) return 0; // guard clause for empty input

        deleteAppointmentsByPatientIds(ids); // remove dependent appointments first

        StringBuilder sb = new StringBuilder(); // builds SQL placeholders
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("?");
        }

        String sql = "DELETE FROM patients WHERE id IN (" + sb + ")"; // delete query

        try (Connection conn = DBConnection.getConnection(); // open DB connection
             PreparedStatement ps = conn.prepareStatement(sql)) { // prepare statement

            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i)); // bind patient ids
            }
            return ps.executeUpdate();        // execute delete and return affected rows
        }
    }

    private int deleteAppointmentsByPatientIds(List<Integer> patientIds) throws SQLException { // deletes related appointments
        if (patientIds == null || patientIds.isEmpty()) return 0; // guard clause

        StringBuilder sb = new StringBuilder(); // builds SQL placeholders
        for (int i = 0; i < patientIds.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("?");
        }

        String sql = "DELETE FROM appointments WHERE patient_id IN (" + sb + ")"; // delete appointments query

        try (Connection conn = DBConnection.getConnection(); // open DB connection
             PreparedStatement ps = conn.prepareStatement(sql)) { // prepare statement

            for (int i = 0; i < patientIds.size(); i++) {
                ps.setInt(i + 1, patientIds.get(i)); // bind patient ids
            }
            return ps.executeUpdate();                // execute delete and return affected rows
        }
    }
}
