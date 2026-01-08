// src/main/java/com/hospital/ui/DoctorsPanel.java
package com.hospital.ui; // UI package for doctor management

import java.awt.BorderLayout; // layout manager for north/south/east/west positioning
import java.awt.FlowLayout; // flow layout for button panels
import java.awt.GridBagConstraints; // grid constraints for form layout
import java.awt.GridBagLayout; // grid bag layout for flexible form positioning
import java.awt.Insets; // padding/margins for components
import java.sql.SQLException; // database exception
import java.util.ArrayList; // list for storing doctor IDs
import java.util.List; // list interface

import javax.swing.BorderFactory; // factory for borders
import javax.swing.JButton; // button component
import javax.swing.JLabel; // label for text display
import javax.swing.JOptionPane; // dialog for user messages
import javax.swing.JPanel; // panel container
import javax.swing.JScrollPane; // scrollable container
import javax.swing.JTable; // table component
import javax.swing.JTextField; // text input field
import javax.swing.table.DefaultTableModel; // default table data model

import com.hospital.dao.DoctorDAO; // data access object for doctors
import com.hospital.model.Doctor; // doctor model class

public class DoctorsPanel extends JPanel { // panel for managing doctors UI

    private final DoctorDAO doctorDAO = new DoctorDAO(); // DAO for doctor database operations

    private JTextField firstNameField; // input field for first name
    private JTextField lastNameField; // input field for last name
    private JTextField specializationField; // input field for specialization
    private JTextField phoneField; // input field for phone number

    private JTable table; // table to display doctor records
    private DefaultTableModel tableModel; // model for table data

    public DoctorsPanel() { // constructor initializes UI components
        setLayout(new BorderLayout(10, 10)); // set main layout with gaps
        add(buildTopForm(), BorderLayout.NORTH); // add form at top
        add(buildTableArea(), BorderLayout.CENTER); // add table in center

        refreshTable(); // load initial doctor data
    }

    private JPanel buildTopForm() { // create form panel with inputs
        JPanel outer = new JPanel(new BorderLayout()); // outer container
        outer.setBorder(BorderFactory.createTitledBorder("Add Doctor")); // add title border

        JPanel form = new JPanel(new GridBagLayout()); // grid-based form layout
        GridBagConstraints gbc = new GridBagConstraints(); // constraints for component placement
        gbc.insets = new Insets(6, 8, 6, 8); // set padding around components
        gbc.anchor = GridBagConstraints.WEST; // align components to the left
        gbc.fill = GridBagConstraints.HORIZONTAL; // make components fill horizontally

        firstNameField = new JTextField(18); // create text field
        lastNameField = new JTextField(18); // create text field
        specializationField = new JTextField(18); // create text field
        phoneField = new JTextField(18); // create text field

        int row = 0; // track current row in grid

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; // set column 0, weight for label
        form.add(new JLabel("First Name:"), gbc); // add label
        gbc.gridx = 1; gbc.weightx = 1; // set column 1 with expand weight
        form.add(firstNameField, gbc); // add text field

        gbc.gridx = 2; gbc.weightx = 0; // set column 2 for next label
        form.add(new JLabel("Last Name:"), gbc); // add label
        gbc.gridx = 3; gbc.weightx = 1; // set column 3 with expand weight
        form.add(lastNameField, gbc); // add text field

        row++; // move to next row

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; // set column 0, new row
        form.add(new JLabel("Specialization:"), gbc); // add label
        gbc.gridx = 1; gbc.weightx = 1; // set column 1 with expand weight
        form.add(specializationField, gbc); // add text field

        gbc.gridx = 2; gbc.weightx = 0; // set column 2 for next label
        form.add(new JLabel("Phone:"), gbc); // add label
        gbc.gridx = 3; gbc.weightx = 1; // set column 3 with expand weight
        form.add(phoneField, gbc); // add text field

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); // button panel, right-aligned
        JButton addBtn = new JButton("Add"); // create add button
        JButton refreshBtn = new JButton("Refresh"); // create refresh button
        JButton deleteBtn = new JButton("Delete Selected"); // create delete button

        btns.add(addBtn); // add button to panel
        btns.add(refreshBtn); // add button to panel
        btns.add(deleteBtn); // add button to panel

        addBtn.addActionListener(e -> onAdd()); // bind click to add method
        refreshBtn.addActionListener(e -> refreshTable()); // bind click to refresh method
        deleteBtn.addActionListener(e -> onDeleteSelected()); // bind click to delete method

        outer.add(form, BorderLayout.CENTER); // add form to center
        outer.add(btns, BorderLayout.SOUTH); // add buttons to bottom
        return outer; // return constructed panel
    }

    private JScrollPane buildTableArea() { // create table area with columns
        tableModel = new DefaultTableModel( // create table model
                new Object[]{"ID", "First Name", "Last Name", "Specialization", "Phone"}, 0 // columns and rows
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; } // disable cell editing
        };

        table = new JTable(tableModel); // create table with model
        table.setRowHeight(26); // set row height for readability

        JScrollPane sp = new JScrollPane(table); // wrap table in scroll pane
        sp.setBorder(BorderFactory.createTitledBorder("Doctors List")); // add border title
        return sp; // return scroll pane
    }

    private void onAdd() { // handle add doctor button click
        String fn = firstNameField.getText().trim(); // get first name
        String ln = lastNameField.getText().trim(); // get last name
        String sp = specializationField.getText().trim(); // get specialization
        String ph = phoneField.getText().trim(); // get phone

        if (fn.isEmpty() || ln.isEmpty()) { // validate required fields
            JOptionPane.showMessageDialog(this, "First Name and Last Name are required.", "Validation", JOptionPane.WARNING_MESSAGE); // show error
            return; // exit early
        }

        Doctor d = new Doctor(null, fn, ln, sp.isEmpty() ? null : sp, ph.isEmpty() ? null : ph); // create doctor object

        try {
            doctorDAO.create(d); // insert into database
            clearForm(); // reset form
            refreshTable(); // reload table
        } catch (SQLException ex) { // handle database error
            JOptionPane.showMessageDialog(this, "Failed to add doctor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // show error
        }
    }

    private void onDeleteSelected() { // handle delete button click
        int[] rows = table.getSelectedRows(); // get selected row indices
        if (rows == null || rows.length == 0) { // check if any row selected
            JOptionPane.showMessageDialog(this, "Select at least one doctor to delete.", "Info", JOptionPane.INFORMATION_MESSAGE); // show info
            return; // exit early
        }

        int confirm = JOptionPane.showConfirmDialog( // show confirmation dialog
                this,
                "Delete selected doctors?\n(Their appointments will be deleted too.)",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return; // exit if not confirmed

        List<Integer> ids = new ArrayList<>(); // list to hold doctor IDs
        for (int r : rows) { // iterate through selected rows
            Object idObj = tableModel.getValueAt(r, 0); // get ID from column 0
            if (idObj instanceof Number n) ids.add(n.intValue()); // add if number
            else if (idObj != null) ids.add(Integer.parseInt(idObj.toString())); // parse and add
        }

        try {
            int deleted = doctorDAO.deleteByIds(ids); // delete from database
            refreshTable(); // reload table
            JOptionPane.showMessageDialog(this, "Deleted: " + deleted); // show count
        } catch (SQLException ex) { // handle database error
            JOptionPane.showMessageDialog(this, "Failed to delete doctors: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // show error
        }
    }

    private void refreshTable() { // reload table with current data
        try {
            List<Doctor> list = doctorDAO.listAll(); // fetch all doctors
            tableModel.setRowCount(0); // clear table rows

            for (Doctor d : list) { // iterate through doctors
                tableModel.addRow(new Object[]{ // add row to table
                        d.getId(), // doctor ID
                        d.getFirstName(), // first name
                        d.getLastName(), // last name
                        d.getSpecialization() == null ? "" : d.getSpecialization(), // specialization (empty if null)
                        d.getPhone() == null ? "" : d.getPhone() // phone (empty if null)
                });
            }
        } catch (SQLException ex) { // handle database error
            JOptionPane.showMessageDialog(this, "Failed to load doctors: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // show error
        }
    }

    private void clearForm() { // reset all input fields
        firstNameField.setText(""); // clear first name
        lastNameField.setText(""); // clear last name
        specializationField.setText(""); // clear specialization
        phoneField.setText(""); // clear phone
    }
}
