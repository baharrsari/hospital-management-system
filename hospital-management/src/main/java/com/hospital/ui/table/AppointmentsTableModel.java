package com.hospital.ui.table; // table model package

import java.sql.Date; // SQL date type
import java.sql.Time; // SQL time type
import java.util.ArrayList; // list implementation
import java.util.List; // list interface

import javax.swing.table.AbstractTableModel; // base table model

import com.hospital.dao.AppointmentDAO.AppointmentRow; // appointment row record

public class AppointmentsTableModel extends AbstractTableModel { // custom table model for appointments

    private final String[] columns = { "ID", "Patient", "Doctor", "Date", "Time", "Notes" }; // column headers
    private List<AppointmentRow> rows = new ArrayList<>(); // table data rows

    public void setRows(List<AppointmentRow> rows) { // update table rows
        this.rows = (rows == null) ? new ArrayList<>() : new ArrayList<>(rows); // set or use empty list
        fireTableDataChanged(); // notify listeners of change
    }

    public AppointmentRow getRow(int modelIndex) { // get row by index
        if (modelIndex < 0 || modelIndex >= rows.size()) return null; // bounds check
        return rows.get(modelIndex); // return row
    }

    @Override
    public int getRowCount() { // get number of rows
        return rows.size(); // return row count
    }

    @Override
    public int getColumnCount() { // get number of columns
        return columns.length; // return column count
    }

    @Override
    public String getColumnName(int column) { // get column header
        return columns[column]; // return column name
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) { // get column data type
        return switch (columnIndex) { // switch on column index
            case 0 -> Integer.class; // ID is integer
            case 3 -> Date.class; // Date column
            case 4 -> Time.class; // Time column
            default -> String.class; // all others are string
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) { // get cell value
        AppointmentRow r = rows.get(rowIndex); // get row
        return switch (columnIndex) { // switch on column
            case 0 -> r.id(); // return ID
            case 1 -> r.patientName(); // return patient name
            case 2 -> r.doctorName(); // return doctor name
            case 3 -> r.date(); // return date
            case 4 -> r.time(); // return time
            case 5 -> r.notes(); // return notes
            default -> ""; // default empty
        };
    }
}
