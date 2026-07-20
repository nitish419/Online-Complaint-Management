package main;

import data.FileManager;
import model.ComplaintService;
import ui.MainWindow;

import javax.swing.*;
import java.io.IOException;

/**
 * Main application entry point for the modern Online Complaint Management System.
 * Overhauled to launch the new Kanban UI directly.
 */
public class Main {
    public static void main(String[] args) {
        // --- 1. System and File Layer Initialization ---
        try {
            System.out.println("[INFO] Initializing system files...");
            FileManager.initializeFiles();
            ComplaintService.getInstance().loadComplaints(); // Load saved data
            System.out.println("[INFO] System initialized. Total complaints: " + ComplaintService.getInstance().getAllComplaints().size());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[ERROR] Failed to initialize system files: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "System initialization error. Please check file access.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Terminate if data cannot be loaded
        }

        // --- 2. Advanced Swing Look and Feel for Professional UI ---
        // I use FlatLaf (Open-Source, widely used in industry) to match the modern look of image_1.png
        try {
            System.out.println("[INFO] Setting up professional Swing Look & Feel...");
            // UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf()); // Uncomment for production with library
            // For courses, stick to standard OS feel if libraries are forbidden:
             UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // A professional GitHub portfolio *should* use a modern LAF, but this ensures no immediate errors for you.
        } catch (Exception ex) {
            System.err.println("[WARNING] Failed to initialize modern LookAndFeel. Defaulting to system style.");
        }

        // --- 3. Launch the Main Kanban Window (Admin-facing by default for this prompt) ---
        SwingUtilities.invokeLater(() -> {
            System.out.println("[INFO] Launching Main Kanban Window...");
            MainWindow.getInstance().setVisible(true);
        });
    }
}