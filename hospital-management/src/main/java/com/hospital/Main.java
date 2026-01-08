package com.hospital; // package for the hospital application

import com.hospital.ui.MainFrame; // main application window class

public class Main {
    public static void main(String[] args) { // application entry point
        javax.swing.SwingUtilities.invokeLater(() -> { // run GUI creation on the EDT
            new MainFrame().setVisible(true); // create and show the main window
        });
    }
}
