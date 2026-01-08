// src/main/java/com/hospital/ui/AppointmentsPanel.java
package com.hospital.ui; // UI package

import java.awt.BorderLayout; // border layout manager
import java.awt.Dimension; // dimension for component sizing
import java.awt.FlowLayout; // flow layout for button arrangement
import java.sql.Date; // SQL date type
import java.sql.Time; // SQL time type
import java.text.SimpleDateFormat; // date formatting utility
import java.util.ArrayList; // list for storing IDs
import java.util.List; // list interface
import java.util.Objects; // object utility methods

import javax.swing.BorderFactory; // factory for borders
import javax.swing.DefaultComboBoxModel; // model for combo boxes
import javax.swing.JButton; // button component
import javax.swing.JComboBox; // dropdown/combo box component
import javax.swing.JLabel; // label for text display
import javax.swing.JOptionPane; // dialog for user messages
import javax.swing.JPanel; // panel container
import javax.swing.JScrollPane; // scrollable container
import javax.swing.JSpinner; // spinner for date/time input
import javax.swing.JTable; // table component
import javax.swing.JTextArea; // multi-line text area
import javax.swing.SpinnerDateModel; // spinner model for dates
import javax.swing.border.EmptyBorder; // border with padding
import javax.swing.table.DefaultTableCellRenderer; // table cell renderer
import javax.swing.table.TableRowSorter; // row sorter for table

import com.hospital.dao.AppointmentDAO; // DAO for appointments
import com.hospital.dao.AppointmentDAO.AppointmentRow; // appointment row record
import com.hospital.dao.DoctorDAO; // DAO for doctors
import com.hospital.dao.PatientDAO; // DAO for patients
import com.hospital.model.Doctor; // doctor model
import com.hospital.model.Patient; // patient model
import com.hospital.ui.table.AppointmentsTableModel; // custom table model

public class AppointmentsPanel extends JPanel { // panel for managing appointments

    private final AppointmentDAO appointmentDAO = new AppointmentDAO(); // DAO for appointment operations
    private final PatientDAO patientDAO = new PatientDAO(); // DAO for patient operations
    private final DoctorDAO doctorDAO = new DoctorDAO(); // DAO for doctor operations

    private final JComboBox<Patient> patientCombo = new JComboBox<>(); // dropdown for patient selection
    private final JComboBox<Doctor> doctorCombo = new JComboBox<>(); // dropdown for doctor selection

    private final JSpinner dateSpinner = new JSpinner(new SpinnerDateModel()); // spinner for date input
    private final JSpinner timeSpinner = new JSpinner(new SpinnerDateModel()); // spinner for time input

    private final JTextArea notesArea = new JTextArea(4, 20); // text area for appointment notes

    private final AppointmentsTableModel tableModel = new AppointmentsTableModel(); // table model
    private final JTable table = new JTable(tableModel); // table to display appointments

    private final JButton btnRefresh = new JButton("Refresh"); // refresh button
    private final JButton btnCreate = new JButton("Create Appointment"); // create appointment button
    private final JButton btnDelete = new JButton("Delete Selected"); // delete appointment button

    public AppointmentsPanel() { // constructor initializes the panel
        setLayout(new BorderLayout(14, 14)); // set main layout
        setBorder(new EmptyBorder(14, 14, 14, 14)); // set padding border

        add(buildHeader(), BorderLayout.NORTH); // add header
        add(buildCenter(), BorderLayout.CENTER); // add center content

        wireUI(); // bind UI events
        reloadAll(); // load initial data
    }

    private JPanel buildHeader() { // create header with title and buttons
        JPanel header = new JPanel(new BorderLayout()); // header container
        header.setBorder(new EmptyBorder(0, 0, 6, 0)); // padding below

        JLabel title = new JLabel("Appointments"); // title label
        title.setBorder(new EmptyBorder(0, 2, 0, 0)); // padding
        title.setFont(title.getFont().deriveFont(18f)); // enlarge font

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); // button panel
        actions.add(btnRefresh); // add refresh button
        actions.add(btnDelete); // add delete button

        header.add(title, BorderLayout.WEST); // add title to left
        header.add(actions, BorderLayout.EAST); // add buttons to right
        return header; // return constructed header
    }

    private JPanel buildCenter() { // create main content area
        JPanel center = new JPanel(new BorderLayout(14, 14)); // center container

        // Left: form card for creating appointments
        JPanel formCard = new JPanel(new BorderLayout(10, 10)); // form container
        formCard.setBorder(BorderFactory.createTitledBorder("Create Appointment")); // add title
        formCard.setPreferredSize(new Dimension(380, 10)); // set width

        JPanel form = new JPanel(); // form panel
        form.setLayout(new javax.swing.BoxLayout(form, javax.swing.BoxLayout.Y_AXIS)); // vertical layout
        form.setBorder(new EmptyBorder(10, 10, 10, 10)); // padding

        // Configure spinner formats
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd")); // date format
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm")); // time format

        notesArea.setLineWrap(true); // enable line wrapping
        notesArea.setWrapStyleWord(true); // wrap at word boundaries

        form.add(row("Patient", patientCombo)); // add patient row
        form.add(gap(8)); // spacing
        form.add(row("Doctor", doctorCombo)); // add doctor row
        form.add(gap(8)); // spacing
        form.add(row("Date", dateSpinner)); // add date row
        form.add(gap(8)); // spacing
        form.add(row("Time", timeSpinner)); // add time row
        form.add(gap(8)); // spacing

        JPanel notesWrap = new JPanel(new BorderLayout(6, 6)); // notes wrapper
        notesWrap.add(new JLabel("Notes"), BorderLayout.NORTH); // label
        notesWrap.add(new JScrollPane(notesArea), BorderLayout.CENTER); // scrollable text area
        notesWrap.setBorder(new EmptyBorder(0, 0, 6, 0)); // padding
        form.add(notesWrap); // add to form

        JPanel createRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); // button row
        createRow.add(btnCreate); // add create button
        form.add(createRow); // add to form

        formCard.add(form, BorderLayout.CENTER); // add form to card

        // Right: table card for listing appointments
        JPanel tableCard = new JPanel(new BorderLayout(8, 8)); // table container
        tableCard.setBorder(BorderFactory.createTitledBorder("Latest Appointments")); // add title

        table.setRowHeight(26); // set row height
        table.setAutoCreateRowSorter(true); // enable sorting
        table.setRowSorter(new TableRowSorter<>(tableModel)); // add sorter

        // Time column renderer to format as HH:mm
        SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm"); // time format
        table.setDefaultRenderer(Time.class, new DefaultTableCellRenderer() { // custom renderer
            @Override
            protected void setValue(Object value) { // override value display
                if (value instanceof Time t) { // check if time type
                    setText(timeFmt.format(t)); // format time
                } else {
                    super.setValue(value); // default behavior
                }
            }
        });

        tableCard.add(new JScrollPane(table), BorderLayout.CENTER); // add table to card

        center.add(formCard, BorderLayout.WEST); // add form on left
        center.add(tableCard, BorderLayout.CENTER); // add table on right

        return center; // return constructed center
    }

    private JPanel row(String label, java.awt.Component comp) { // create labeled input row
        JPanel p = new JPanel(new BorderLayout(8, 6)); // row container
        JLabel l = new JLabel(label); // create label
        l.setPreferredSize(new Dimension(70, 24)); // set label width
        p.add(l, BorderLayout.WEST); // add label to left
        p.add(comp, BorderLayout.CENTER); // add component to center
        return p; // return row
    }

    private JPanel gap(int h) { // create vertical spacing panel
        JPanel p = new JPanel(); // spacer panel
        p.setPreferredSize(new Dimension(1, h)); // set height
        p.setOpaque(false); // transparent
        return p; // return spacer
    }

    private void wireUI() { // bind UI event listeners
        btnRefresh.addActionListener(e -> reloadAll()); // refresh button: reload all data

        btnCreate.addActionListener(e -> { // create appointment button
            try {
                Patient p = (Patient) patientCombo.getSelectedItem(); // get selected patient
                Doctor d = (Doctor) doctorCombo.getSelectedItem(); // get selected doctor

                if (p == null || p.getId() == null || d == null || d.getId() == null) { // validate selection
                    JOptionPane.showMessageDialog(this, "Please select patient and doctor."); // error message
                    return; // exit
                }

                Date sqlDate = toSqlDate((java.util.Date) dateSpinner.getValue()); // convert date
                Time sqlTime = toSqlTime((java.util.Date) timeSpinner.getValue()); // convert time
                String notes = notesArea.getText(); // get notes

                appointmentDAO.create(p.getId(), d.getId(), sqlDate, sqlTime, notes); // save to database

                notesArea.setText(""); // clear form
                reloadTableOnly(); // reload table

                JOptionPane.showMessageDialog(this, "Appointment created."); // success message
            } catch (Exception ex) { // handle errors
                showError(ex); // show error
            }
        });

        btnDelete.addActionListener(e -> { // delete appointment button
            try {
                int[] viewRows = table.getSelectedRows(); // get selected rows
                if (viewRows == null || viewRows.length == 0) { // check selection
                    JOptionPane.showMessageDialog(this, "Select at least one row to delete."); // info
                    return; // exit
                }

                int confirm = JOptionPane.showConfirmDialog( // show confirmation
                        this,
                        "Delete selected appointments?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm != JOptionPane.YES_OPTION) return; // exit if not confirmed

                List<Integer> ids = new ArrayList<>(); // list for appointment IDs
                for (int vr : viewRows) { // iterate selected rows
                    int mr = table.convertRowIndexToModel(vr); // convert view index to model
                    AppointmentRow row = tableModel.getRow(mr); // get row data
                    if (row != null) ids.add(row.id()); // collect ID
                }

                int deleted = appointmentDAO.deleteByIds(ids); // delete from database
                reloadTableOnly(); // reload table

                JOptionPane.showMessageDialog(this, "Deleted: " + deleted); // show count
            } catch (Exception ex) { // handle errors
                showError(ex); // show error
            }
        });
    }

    private void reloadAll() { // reload combos and table
        reloadCombos(); // reload patient and doctor dropdowns
        reloadTableOnly(); // reload table
    }

    private void reloadCombos() { // reload patient and doctor dropdowns
        try {
            List<Patient> patients = patientDAO.listAll(); // fetch all patients
            List<Doctor> doctors = doctorDAO.listAll(); // fetch all doctors

            Patient prevP = (Patient) patientCombo.getSelectedItem(); // save selection
            Doctor prevD = (Doctor) doctorCombo.getSelectedItem(); // save selection

            patientCombo.setModel(new DefaultComboBoxModel<>(patients.toArray(new Patient[0]))); // set patient list
            doctorCombo.setModel(new DefaultComboBoxModel<>(doctors.toArray(new Doctor[0]))); // set doctor list

            patientCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> { // custom patient renderer
                JLabel label = new JLabel(formatPatient(value)); // format patient display
                styleComboCell(label, list, isSelected); // apply styling
                return label; // return cell
            });

            doctorCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> { // custom doctor renderer
                JLabel label = new JLabel(formatDoctor(value)); // format doctor display
                styleComboCell(label, list, isSelected); // apply styling
                return label; // return cell
            });

            if (prevP != null) selectPatient(prevP.getId()); // restore patient selection
            if (prevD != null) selectDoctor(prevD.getId()); // restore doctor selection

        } catch (Exception ex) { // handle errors
            showError(ex); // show error
        }
    }

    private void styleComboCell(JLabel label, javax.swing.JList<?> list, boolean isSelected) { // style combo cells
        label.setBorder(new EmptyBorder(4, 8, 4, 8)); // padding
        if (isSelected) { // if selected
            label.setOpaque(true); // enable opaque background
            label.setBackground(list.getSelectionBackground()); // set background color
            label.setForeground(list.getSelectionForeground()); // set text color
        }
    }

    private void reloadTableOnly() { // reload appointment table
        try {
            tableModel.setRows(appointmentDAO.listAllDetailed()); // fetch and set appointments
        } catch (Exception ex) { // handle errors
            showError(ex); // show error
        }
    }

    private void selectPatient(Integer id) { // select patient by ID
        if (id == null) return; // exit if ID is null
        for (int i = 0; i < patientCombo.getItemCount(); i++) { // iterate items
            Patient p = patientCombo.getItemAt(i); // get item
            if (p != null && Objects.equals(p.getId(), id)) { // check match
                patientCombo.setSelectedIndex(i); // select
                return; // exit
            }
        }
    }

    private void selectDoctor(Integer id) { // select doctor by ID
        if (id == null) return; // exit if ID is null
        for (int i = 0; i < doctorCombo.getItemCount(); i++) { // iterate items
            Doctor d = doctorCombo.getItemAt(i); // get item
            if (d != null && Objects.equals(d.getId(), id)) { // check match
                doctorCombo.setSelectedIndex(i); // select
                return; // exit
            }
        }
    }

    private String formatPatient(Patient p) { // format patient for display
        if (p == null) return ""; // return empty if null
        String name = safe(p.getFirstName()) + " " + safe(p.getLastName()); // build name
        return name.trim() + " (ID: " + p.getId() + ")"; // return formatted
    }

    private String formatDoctor(Doctor d) { // format doctor for display
        if (d == null) return ""; // return empty if null
        String name = "Dr. " + safe(d.getFirstName()) + " " + safe(d.getLastName()); // build name with title
        String spec = safe(d.getSpecialization()); // get specialization
        if (!spec.isBlank()) name += " — " + spec; // append specialization
        return name.trim() + " (ID: " + d.getId() + ")"; // return formatted
    }

    private String safe(String s) { // safe null-to-empty conversion
        return (s == null) ? "" : s; // return empty if null, else string
    }

    private static Date toSqlDate(java.util.Date utilDate) { // convert util.Date to sql.Date
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd"); // date format
        String s = f.format(Objects.requireNonNull(utilDate)); // format to string
        return Date.valueOf(s); // convert to SQL date
    }

    private static Time toSqlTime(java.util.Date utilDate) { // convert util.Date to sql.Time
        SimpleDateFormat f = new SimpleDateFormat("HH:mm"); // time format
        String hhmm = f.format(Objects.requireNonNull(utilDate)); // format to string
        return Time.valueOf(hhmm + ":00"); // convert to SQL time with seconds
    }

    private void showError(Exception ex) { // display error dialog
        ex.printStackTrace(); // print stack trace
        JOptionPane.showMessageDialog( // show dialog
                this,
                ex.getClass().getSimpleName() + ": " + ex.getMessage(), // error message
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
