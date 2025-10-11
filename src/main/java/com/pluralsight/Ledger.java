package com.pluralsight;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.pluralsight.FinancialTrackerForPrince.showHomeScreen;

public class Ledger {
    //Starts the app and runs the main menu loop
    static Scanner scanner = new Scanner(System.in);

    public static boolean displayLedger() {
        List<Transaction> transactions = TransactionFileManager.loadTransactions();
        String options = """
                PRINCE!
                Would you like to view...
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
                System.out.println("Goodbye Prince!");
                System.exit(0);
                break;
            default:
                System.out.println("Prince, that's not an option.");
                break;
        }
        return false;
    }

    public static void displayReports(List<Transaction> transactions) {
        boolean stayInReports = true;
        while (stayInReports) {
            System.out.println("""
                    PRINCE
                    What reports would you like to see displayed?
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
                    stayInReports = false;
                    break;
                default:
                    System.out.println("Prince, that's not an option.");
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
                PRINCE
                What custom search would you like to make?
                S) Start Date
                E) End Date
                D) Description
                V) Vendor
                A) Amount
                """;
            String choice = getLetterChoice(options);
            switch (choice) {
                case "S":
                    System.out.println("Prince, enter the start date (yyyy-MM-dd) or press enter to skip");
                    startdate2 = scanner.nextLine().trim();
                    String finalStartDate = startdate2;
                    List<Transaction> filteredByStartDate = transactions.stream()
                            .filter(t -> (finalStartDate.isEmpty() || t.getDate().isEqual(LocalDate.parse(finalStartDate))))
                            .toList();
                    printTransactions(filteredByStartDate);
                    break;
                case "E":
                    System.out.println("Prince, enter the end date (yyyy-MM-dd) or press enter to skip");
                    enddate2 = scanner.nextLine().trim();
                    String finalEndDate = enddate2;
                    List<Transaction> filteredByEndDate = transactions.stream()
                            .filter(t -> (finalEndDate.isEmpty() || t.getDate().isEqual(LocalDate.parse(finalEndDate))))
                            .toList();
                    printTransactions(filteredByEndDate);
                    break;
                case "D":
                    System.out.println("Prince, enter the description or press enter to skip");
                    description2 = scanner.nextLine().trim();
                    String finalDescription = description2;
                    List<Transaction> filteredByDescription = transactions.stream()
                            .filter(t -> (finalDescription.isEmpty() || t.getDescription().toLowerCase().contains(finalDescription.toLowerCase())))
                            .toList();
                    printTransactions(filteredByDescription);
                    break;
                case "V":
                    System.out.println("Prince, enter the vendor name or press enter to skip");
                    vendor2 = scanner.nextLine().trim();
                    String finalVendor = vendor2;
                    List<Transaction> filteredByVendor = transactions.stream()
                            .filter(t -> (finalVendor.isEmpty() || t.getVendor().equalsIgnoreCase(finalVendor)))
                            .toList();
                    printTransactions(filteredByVendor);
                    break;
                case "A":
                    System.out.println("Prince, enter the amount or press enter to skip");
                    amount2 = scanner.nextLine().trim();
                    String finalAmount = amount2;
                    List<Transaction> filteredByAmount = transactions.stream()
                            .filter(t -> (finalAmount.isEmpty() || t.getAmount() == Double.parseDouble(finalAmount)))
                            .toList();
                    printTransactions(filteredByAmount);
                    break;
                case "H":
                    showHomeScreen();
                    break;
                case "X":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Prince, that's not an option.");
                    break;
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
            System.out.println("Prince, there are no matching transactions found.");
            return;
        }
        System.out.println("                        WELCOME PRINCE                               ");
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
            System.out.println("Prince, there is no transaction file found yet" + e.getMessage());
            return;
        }
        List<Transaction> transactions = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split("\\|");
            if (parts.length == 5) {
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4].replace("$", "").replace(",", "").trim());

                transactions.add(new Transaction(date, time, description, vendor, amount));
            }
        }
        transactions.sort(
                Comparator.comparing(Transaction::getDate)
                        .thenComparing(Transaction::getTime)
                        .reversed()
        );
        if (lines.isEmpty()) {
            System.out.println("Prince, there are no transactions found.");
            return;
        }
        System.out.println("                        WELCOME PRINCE                               ");
        System.out.println("DATE         TIME       DESCRIPTION           VENDOR           AMOUNT");
        System.out.println("--------------------------------------------------------------------------");
        for (Transaction t : transactions) {
                System.out.printf("%-12s %-10s %-20s %-15s %s%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());            }
        }
    }
