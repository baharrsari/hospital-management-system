package com.hospital.dao;

import java.sql.Connection;          // JDBC connection interface
import java.sql.Date;                // SQL Date type (appointment date)
import java.sql.PreparedStatement;   // precompiled SQL statement
import java.sql.ResultSet;           // result set from SQL query
import java.sql.SQLException;        // SQL exception handling
import java.sql.Time;                // SQL Time type (appointment time)
import java.sql.Types;               // SQL type constants
import java.util.ArrayList;          // dynamic list implementation
import java.util.List;               // list interface

import com.hospital.db.DBConnection; // database connection utility

/**
 * Data Access Object for appointments table.
 */
public class AppointmentDAO {

    /**
     * Simple DTO (data transfer object) for table display.
     */
    public record AppointmentRow(
            int id,                 // appointment id
            String patientName,     // full patient name
            String doctorName,      // full doctor name (with specialization)
            Date date,              // appointment date
            Time time,              // appointment time
            String notes            // optional notes
    ) {}

    public void create(
            int patientId,          // referenced patient id
            int doctorId,           // referenced doctor id
            Date date,              // appointment date
            Time time,              // appointment time
            String notes            // appointment notes
    ) throws SQLException {

        String sql = """
                INSERT INTO appointments
                (patient_id, doctor_id, appointment_date, appointment_time, notes)
                VALUES (?, ?, ?, ?, ?)
                """; // SQL insert query

        try (Connection conn = DBConnection.getConnection(); // open DB connection
             PreparedStatement ps = conn.prepareStatement(sql)) { // prepare statement

            ps.setInt(1, patientId); // bind patient id
            ps.setInt(2, doctorId);  // bind doctor id
            ps.setDate(3, date);     // bind appointment date
            ps.setTime(4, time);     // bind appointment time

            if (notes == null || notes.isBlank()) { // check if notes are empty
                ps.setNull(5, Types.VARCHAR);       // set notes as NULL
            } else {
                ps.setString(5, notes);             // bind notes text
            }

            ps.executeUpdate(); // execute insert query
        }
    }

    /**
     * Returns appointments with patient/doctor names for UI display.
     */
    public List<AppointmentRow> listAllDetailed() throws SQLException { // fetch detailed appointment list

        String sql = """
                SELECT a.id,
                       CONCAT(p.first_name, ' ', p.last_name) AS patient_name,
                       CONCAT(
                           d.first_name, ' ', d.last_name,
                           IFNULL(CONCAT(' (', d.specialization, ')'), '')
                       ) AS doctor_name,
                       a.appointment_date,
                       a.appointment_time,
                       a.notes
                FROM appointments a
                JOIN patients p ON p.id = a.patient_id
                JOIN doctors d ON d.id = a.doctor_id
                ORDER BY a.id DESC
                """; // SQL join query

        List<AppointmentRow> result = new ArrayList<>(); // result list

        try (Connection conn = DBConnection.getConnection(); // open DB connection
             PreparedStatement ps = conn.prepareStatement(sql); // prepare statement
             ResultSet rs = ps.executeQuery()) {               // execute query

            while (rs.next()) {                                // iterate result set
                result.add(new AppointmentRow(
                        rs.getInt("id"),                       // read appointment id
                        rs.getString("patient_name"),          // read patient name
                        rs.getString("doctor_name"),           // read doctor name
                        rs.getDate("appointment_date"),        // read appointment date
                        rs.getTime("appointment_time"),        // read appointment time
                        rs.getString("notes")                  // read notes
                ));
            }
        }
        return result; // return appointment list
    }

    public int deleteByIds(List<Integer> ids) throws SQLException { // deletes appointments by id list
        if (ids == null || ids.isEmpty()) return 0;                 // guard clause

        StringBuilder sb = new StringBuilder(); // build SQL placeholders
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("?");
        }

        String sql = "DELETE FROM appointments WHERE id IN (" + sb + ")"; // delete query

        try (Connection conn = DBConnection.getConnection(); // open DB connection
             PreparedStatement ps = conn.prepareStatement(sql)) { // prepare statement

            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i)); // bind appointment ids
            }
            return ps.executeUpdate();        // execute delete and return affected rows
        }
    }

}
