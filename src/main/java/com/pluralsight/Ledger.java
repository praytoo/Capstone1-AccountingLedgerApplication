package com.pluralsight;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static com.pluralsight.LedgerApp.showHomeScreen;

public class Ledger {
    //Starts the app and runs the main menu loop
    static Scanner scanner = new Scanner(System.in);

    public static boolean displayLedger() {
        List<Transaction> transactions = TransactionFileManager.loadTransactions();
        String options = """
                A) All
                D) Deposits
                P) Payments
                R) Reports
                H) Home
                """;
        switch (getLetterChoice(options)) {
            case "A":
                showLedger();
                break;
            case "D":
                displayDeposits(transactions);
                break;
            case "P":
                displayPayments(transactions);
                break;
            case "R":
                displayReports(transactions);
                break;
            case "H":
                showHomeScreen();
            case "X":
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("That's not an option.");
                break;
        }
        return false;
    }

    public static void displayReports(List<Transaction> transactions) {
        boolean stayInReports = true;
        while (stayInReports) {
            System.out.println("""
                    1) Month To Date
                    2) Previous Month
                    3) Year To Date
                    4) Previous Year
                    5) Search by Vendor
                    6) Custom Search
                    0) Back
                    """);
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    showMonthToDate(transactions);
                    break;
                case "2":
                    showPreviousMonth(transactions);
                    break;
                case "3":
                    showYearToDate(transactions);
                    break;
                case "4":
                    showPreviousYear(transactions);
                    break;
                case "5":
                    searchByVendor(transactions);
                    break;
                case "6":
                    customSearch(transactions);
                    break;
                case "0":
                    stayInReports = false; // go back to Ledger
                    break;
                default:
                    System.out.println("That's not an option.");
            }
        }
    }

    public static void customSearch(List<Transaction> transactions) {
        String startdate2 = "";
        String enddate2 = "";
        String description2 = "";
        String vendor2 = "";
        String amount2 = "";

        String options = """
                S) Start Date
                E) End Date
                D) Description
                V) Vendor
                A) Amount
                """;

        boolean done = false;
        while (!done) {
            String choice = getLetterChoice(options);
            switch (choice) {
                case "S":
                    System.out.println("Enter start date (yyyy-MM-dd) or press enter to skip");
                    startdate2 = scanner.nextLine().trim();
                    break;
                case "E":
                    System.out.println("Enter end date (yyyy-MM-dd) or press enter to skip");
                    enddate2 = scanner.nextLine().trim();
                    break;
                case "D":
                    System.out.println("Enter description or press enter to skip");
                    description2 = scanner.nextLine().trim();
                    break;
                case "V":
                    System.out.println("Enter vendor or press enter to skip");
                    vendor2 = scanner.nextLine().trim();
                    break;
                case "A":
                    System.out.println("Enter amount or press enter to skip");
                    amount2 = scanner.nextLine().trim();
                    break;
                case "H":
                    showHomeScreen();
                    break;
                case "X":
                    System.exit(0);
                    break;
                default:
                    System.out.println("That's not an option.");
                    break;
            }
            final String startdate = "";
            final String enddate = "";
            final String description = "";
            final String vendor = "";
            final String amount = "";
                List<Transaction> customSearch = transactions.stream()
                        .filter(t -> (startdate.isEmpty() || !t.getDate().isBefore(LocalDate.parse(startdate))))
                        .filter(t -> (enddate.isEmpty() || !t.getDate().isAfter(LocalDate.parse(enddate))))
                        .filter(t -> (description.isEmpty() || t.getDescription().toLowerCase().contains(description.toLowerCase())))
                        .filter(t -> (vendor.isEmpty() || t.getVendor().toLowerCase().contains(vendor.toLowerCase())))
                        .filter(t -> (amount.isEmpty() || t.getAmount() == Double.parseDouble(amount)))
                        .toList();
                printTransactions(customSearch);
        }
    }

    public static void showMonthToDate(List<Transaction> transactions) {
        LocalDate now = LocalDate.now();
        List<Transaction> filtered = transactions.stream()
                .filter(t -> t.getDate().getMonth() == now.getMonth() &&
                        t.getDate().getYear() == now.getYear())
                .toList();
        printTransactions(filtered);
    }

    public static void showPreviousMonth(List<Transaction> transactions) {
        LocalDate now = LocalDate.now().minusMonths(1);
        List<Transaction> filtered = transactions.stream()
                .filter(t -> t.getDate().getMonth() == now.getMonth() &&
                        t.getDate().getYear() == now.getYear())
                .toList();
        printTransactions(filtered);
    }

    public static void showYearToDate(List<Transaction> transactions) {
        int currentYear = LocalDate.now().getYear();
        List<Transaction> filtered = transactions.stream()
                .filter(t -> t.getDate().getYear() == currentYear)
                .toList();
        printTransactions(filtered);
    }

    public static void showPreviousYear(List<Transaction> transactions) {
        int lastYear = LocalDate.now().getYear() - 1;
        List<Transaction> filtered = transactions.stream()
                .filter(t -> t.getDate().getYear() == lastYear)
                .toList();
        printTransactions(filtered);
    }

    public static void searchByVendor(List<Transaction> transactions) {
        System.out.print("Enter vendor name: ");
        String vendorName = scanner.nextLine().trim().toLowerCase();

        List<Transaction> filtered = transactions.stream()
                .filter(t -> t.getVendor().toLowerCase().contains(vendorName))
                .toList();
        printTransactions(filtered);
    }

    public static void displayDeposits(List<Transaction> transactions) {
        List<Transaction> deposits = transactions.stream()
                .filter(t -> t.getAmount() > 0)
                .toList();
        printTransactions(deposits);
    }

    public static void displayPayments(List<Transaction> transactions) {
        List<Transaction> payments = transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .toList();
        printTransactions(payments);
    }

    private static void printTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No matching transactions found.");
            return;
        }
        System.out.println("DATE         TIME       DESCRIPTION           VENDOR           AMOUNT");
        System.out.println("--------------------------------------------------------------------------");
        for (Transaction t : transactions) {
            System.out.printf("%-12s %-10s %-20s %-15s %.2f%n",
                    t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
        }
    }

    public static String getLetterChoice(String options) {
        System.out.println(options);
        String choice = scanner.nextLine();
        return choice;
    }

    public static void showLedger() {
        Path path = Paths.get("transactions.csv");
        List<String> lines = null;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            System.out.println("No transaction file found yet" + e.getMessage());
            return;
        }
        if (lines.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        System.out.println("DATE         TIME       DESCRIPTION           VENDOR           AMOUNT");
        System.out.println("--------------------------------------------------------------------------");
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split("\\|");
            if (parts.length == 5) {
                System.out.printf("%-12s %-10s %-20s %-15s %s%n",
                        parts[0], parts[1], parts[2], parts[3], parts[4]);
            }
        }


    }
}