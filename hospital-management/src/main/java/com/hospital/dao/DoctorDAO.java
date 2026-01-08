// src/main/java/com/hospital/dao/DoctorDAO.java // DAO class for doctor database operations
package com.hospital.dao; // data access object package

import java.sql.Connection; // database connection
import java.sql.PreparedStatement; // prepared SQL statement
import java.sql.ResultSet; // query results
import java.sql.SQLException; // SQL exception
import java.sql.Statement; // SQL statement
import java.util.ArrayList; // list implementation
import java.util.List; // list interface

import com.hospital.db.DBConnection; // database connection helper
import com.hospital.model.Doctor; // doctor model

public class DoctorDAO { // data access object for doctors

    // Insert a new doctor record into the database with auto-generated ID
    public void create(Doctor doctor) throws SQLException {
        String sql = """
                INSERT INTO doctors (first_name, last_name, specialization, phone)
                VALUES (?, ?, ?, ?)""";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, doctor.getFirstName());
            ps.setString(2, doctor.getLastName());
            ps.setString(3, doctor.getSpecialization());
            ps.setString(4, doctor.getPhone());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) doctor.setId(rs.getInt(1));
            }
        }
    }

    public List<Doctor> listAll() throws SQLException {
        String sql = """
                SELECT id, first_name, last_name, specialization, phone
                FROM doctors
                ORDER BY id DESC
                """;

        List<Doctor> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("specialization"),
                        rs.getString("phone")
                ));
            }
        }

        return result;
    }

    /**
     * Deletes doctors by ids.
     * NOTE: If appointments reference doctors, delete those appointments first.
     */
    public int deleteByIds(List<Integer> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) return 0;

        // 1) delete appointments that reference these doctors (avoid FK errors)
        deleteAppointmentsByDoctorIds(ids);

        // 2) delete doctors
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("?");
        }

        String sql = "DELETE FROM doctors WHERE id IN (" + sb + ")";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i));
            }
            return ps.executeUpdate();
        }
    }

    private int deleteAppointmentsByDoctorIds(List<Integer> doctorIds) throws SQLException {
        if (doctorIds == null || doctorIds.isEmpty()) return 0;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < doctorIds.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("?");
        }

        String sql = "DELETE FROM appointments WHERE doctor_id IN (" + sb + ")";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < doctorIds.size(); i++) {
                ps.setInt(i + 1, doctorIds.get(i));
            }
            return ps.executeUpdate();
        }
    }
}
