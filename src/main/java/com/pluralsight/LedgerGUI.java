package com.pluralsight;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LedgerGUI {

    private JTextArea ledgerArea;
    private JFrame frame;

    public LedgerGUI() {
        initialize();
    }

    private void initialize() {
        // Create the main frame
        frame = new JFrame("Prince's Financial Ledger");
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create top panel with buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addDepositButton = new JButton("Add Deposit");
        JButton addPaymentButton = new JButton("Add Payment");
        JButton refreshButton = new JButton("Refresh Ledger");

        topPanel.add(addDepositButton);
        topPanel.add(addPaymentButton);
        topPanel.add(refreshButton);
        frame.add(topPanel, BorderLayout.NORTH);

        // Ledger display area
        ledgerArea = new JTextArea();
        ledgerArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(ledgerArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Button action listeners for add deposit add payment and refresh ledger

        // Add Deposit
        addDepositButton.addActionListener(e -> {
            try {
                String amountStr = JOptionPane.showInputDialog(frame, "Enter deposit amount:");
                if (amountStr == null) return; // user canceled
                double amount = Double.parseDouble(amountStr);
                while (amount <= 0) {
                    amountStr = JOptionPane.showInputDialog(frame, "Invalid amount. Enter a positive number:");
                    if (amountStr == null) return;
                    amount = Double.parseDouble(amountStr);
                }

                String vendor = JOptionPane.showInputDialog(frame, "Enter vendor:");
                if (vendor == null) return;
                String description = JOptionPane.showInputDialog(frame, "Enter description:");
                if (description == null) return;

                TransactionFileManager.recordTransaction(amount, vendor, description, true);
                JOptionPane.showMessageDialog(frame, "Deposit recorded successfully!");
                refreshLedger();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid number entered.");
            }
        });

        // Add Payment
        addPaymentButton.addActionListener(e -> {
            try {
                String amountStr = JOptionPane.showInputDialog(frame, "Enter payment amount:");
                if (amountStr == null) return;
                double amount = Double.parseDouble(amountStr);
                while (amount <= 0) {
                    amountStr = JOptionPane.showInputDialog(frame, "Invalid amount. Enter a positive number:");
                    if (amountStr == null) return;
                    amount = Double.parseDouble(amountStr);
                }

                String vendor = JOptionPane.showInputDialog(frame, "Enter vendor:");
                if (vendor == null) return;
                String description = JOptionPane.showInputDialog(frame, "Enter description:");
                if (description == null) return;

                TransactionFileManager.recordTransaction(amount, vendor, description, false);
                JOptionPane.showMessageDialog(frame, "Payment recorded successfully!");
                refreshLedger();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid number entered.");
            }
        });

        // Refresh Ledger
        refreshButton.addActionListener(e -> refreshLedger());

        // Make frame visible
        frame.setVisible(true);
    }

    // Refresh ledger display
    private void refreshLedger() {
        List<Transaction> transactions = TransactionFileManager.loadTransactions();
        ledgerArea.setText("");

        if (transactions.isEmpty()) {
            ledgerArea.setText("No transactions found.");
        } else {
            ledgerArea.append(String.format("%-12s %-10s %-20s %-15s %s%n",
                    "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT"));
            ledgerArea.append("------------------------------------------------------------------------------------------------\n");

            for (Transaction t : transactions) {
                ledgerArea.append(String.format("%-12s %-10s %-20s %-15s %.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount()));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LedgerGUI::new);
    }
}
