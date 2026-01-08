package com.hospital.ui; // UI package

import java.awt.Font; // font for text styling
import java.awt.GridBagLayout; // layout manager

import javax.swing.JLabel; // label component
import javax.swing.JPanel; // panel container

public class HomePanel extends JPanel { // home page panel

    public HomePanel() { // constructor
        setLayout(new GridBagLayout()); // set layout to grid bag

        JLabel title = new JLabel("Hospital Management System"); // create title label
        title.setFont(new Font("SansSerif", Font.BOLD, 28)); // set font: sans serif, bold, size 28

        add(title); // add title to panel
    }
}
