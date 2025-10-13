package com.pluralsight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class LedgerGUI {
    public LedgerGUI() {
        initialize();
    }

    private void initialize() {
        // Create the main frame
        JFrame frame = new JFrame("Prince's Ledger");
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600); // example dimensions
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create top panel with buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addDepositButton = new JButton("Add Deposit");
        JButton addPaymentButton = new JButton("Add Payment");
        JButton refreshButton = new JButton("Refresh Ledger");

        //Add buttons to top panel
        topPanel.add(addDepositButton);
        topPanel.add(addPaymentButton);
        topPanel.add(refreshButton);

        //Add panel to frame
        frame.add(topPanel, BorderLayout.NORTH);

        //Add center display area (e.g. transaction list)
        JTextArea ledgerArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(ledgerArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Deposit button
        addDepositButton.addActionListener(e -> {
            TransactionFileManager.addDeposit(); // calls your backend deposit method
            JOptionPane.showMessageDialog(frame, "Deposit recorded successfully!");
        });

        // Payment button
        addPaymentButton.addActionListener(e -> {
            TransactionFileManager.addPayment(); // calls your backend payment method
            JOptionPane.showMessageDialog(frame, "Payment recorded successfully!");
        });

        // Refresh button
        refreshButton.addActionListener(e -> {
            // Load transactions from backend
            var transactions = TransactionFileManager.loadTransactions();

            // Clear and update ledgerArea
            ledgerArea.setText(""); // reset text area
            if (transactions.isEmpty()) {
                ledgerArea.setText("No transactions found.");
            } else {
                for (var t : transactions) {
                    ledgerArea.append(String.format(
                            "%s %s %-20s %-15s %.2f%n",
                            t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount()
                    ));
                }
            }
        });

        //Make frame visible
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LedgerGUI());
    }
}
