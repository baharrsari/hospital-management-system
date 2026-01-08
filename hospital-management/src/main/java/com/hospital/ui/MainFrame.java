package com.hospital.ui; // UI package for main application window

import java.awt.CardLayout; // Swing GUI components

import javax.swing.JFrame; // AWT layout managers
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * Main application window.
 * Uses a CardLayout to switch between modules (Patients / Doctors / Appointments).
 */
public class MainFrame extends JFrame { // main window frame for the app

    private final CardLayout cardLayout; // layout manager to switch between panels
    private final JPanel contentPanel; // container for cards

    public MainFrame() { // constructor initializes the frame
        setTitle("Hospital Management System"); // set window title
        setSize(1000, 650); // set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit on close
        setLocationRelativeTo(null); // center window on screen

        cardLayout = new CardLayout(); // create layout manager
        contentPanel = new JPanel(cardLayout); // create panel with card layout

        setJMenuBar(buildMenuBar()); // set menu bar
        buildCards(); // populate cards

        setContentPane(contentPanel); // set main content
        showCard(Cards.HOME); // display home card
    }

    private JMenuBar buildMenuBar() { // create top menu bar
        JMenuBar menuBar = new JMenuBar(); // create menu bar

        JMenu menu = new JMenu("Modules"); // create modules menu

        JMenuItem patientsItem = new JMenuItem("Patients"); // create patients menu item
        patientsItem.addActionListener(e -> showCard(Cards.PATIENTS)); // show patients card on click

        JMenuItem doctorsItem = new JMenuItem("Doctors"); // create doctors menu item
        doctorsItem.addActionListener(e -> showCard(Cards.DOCTORS)); // show doctors card on click

        JMenuItem appointmentsItem = new JMenuItem("Appointments"); // create appointments menu item
        appointmentsItem.addActionListener(e -> showCard(Cards.APPOINTMENTS)); // show appointments card on click

        JMenuItem homeItem = new JMenuItem("Home"); // create home menu item
        homeItem.addActionListener(e -> showCard(Cards.HOME)); // show home card on click

        menu.add(homeItem); // add to menu
        menu.addSeparator(); // add separator
        menu.add(patientsItem); // add to menu
        menu.add(doctorsItem); // add to menu
        menu.add(appointmentsItem); // add to menu

        menuBar.add(menu); // add menu to bar
        return menuBar; // return constructed menu bar
    }

    private void buildCards() { // add all module panels to content
        contentPanel.add(new HomePanel(), Cards.HOME); // add home panel with card name
        contentPanel.add(new PatientsPanel(), Cards.PATIENTS); // add patients panel with card name
        contentPanel.add(new DoctorsPanel(), Cards.DOCTORS); // add doctors panel with card name
        contentPanel.add(new AppointmentsPanel(), Cards.APPOINTMENTS); // add appointments panel with card name
    }

    private void showCard(String cardName) { // display a specific card by name
        cardLayout.show(contentPanel, cardName); // switch to the specified card
    }

    /**
     * Card names are centralized to avoid typos across the codebase.
     */
    private static class Cards { // constants for card names
        static final String HOME = "HOME"; // home card identifier
        static final String PATIENTS = "PATIENTS"; // patients card identifier
        static final String DOCTORS = "DOCTORS"; // doctors card identifier
        static final String APPOINTMENTS = "APPOINTMENTS"; // appointments card identifier
    }
}
