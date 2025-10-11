package com.pluralsight;

import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class TransactionFileManager {
    static Scanner scanner = new Scanner(System.in);
    //Handles reading/writing to transactions.csv
    public static boolean addDeposit() {
        System.out.println("How much do you want to deposit?");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Who is the vendor?");
        String vendor = scanner.nextLine();
        System.out.println("What is the description of the deposit?");
        String description = scanner.nextLine();
        while (amount <= 0) {
            System.out.println("Invalid amount, please enter a positive number");
            System.out.println("How much do you want to deposit?");
            amount = scanner.nextDouble();
            scanner.nextLine();
        }
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        date.format(dateFormatter);
        time.format(timeFormatter);

        String dateString = date.format(dateFormatter);
        String timeString = time.format(timeFormatter);

        String line = dateString + "|" + timeString + "|" + description + "|" + vendor + "|" + amount;
        line = line + "\n";
        System.out.println(line);

        Path path = Paths.get("transactions.csv");
        Charset charset = StandardCharsets.UTF_8;
        try (BufferedWriter writer = Files.newBufferedWriter(path, charset, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(line);
            System.out.println("Deposit successfully recorded!");
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to file." + e.getMessage());
            return false;
        }
    }
    public static boolean addPayment(){
        System.out.println("What is the amount of the payment you would like to debit?");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Who is the vendor?");
        String vendor = scanner.nextLine();
        System.out.println("What is the description of the payment?");
        String description = scanner.nextLine();
        while (amount <= 0) {
            System.out.println("Invalid amount, please enter a positive number");
            System.out.println("What is the amount of the payment you would like to debit?");
            amount = scanner.nextDouble();
            scanner.nextLine();
        }
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        date.format(dateFormatter);
        time.format(timeFormatter);

        String dateString = date.format(dateFormatter);
        String timeString = time.format(timeFormatter);

        amount = -amount;
        String line = dateString + "|" + timeString + "|" + description + "|" + vendor + "|" + amount;
        line = line + "\n";
        System.out.println(line);

        Path path = Paths.get("transactions.csv");
        Charset charset = StandardCharsets.UTF_8;
        try (BufferedWriter writer = Files.newBufferedWriter(path, charset, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(line);
            System.out.println("Debit successfully recorded!");
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to file." + e.getMessage());
            return false;
        }
    }
    public static List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        Path path = Paths.get("transactions.csv");

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    LocalDate date = LocalDate.parse(parts[0]);
                    LocalTime time = LocalTime.parse(parts[1]);
                    Transaction t = new Transaction(date, time, parts[2], parts[3], Double.parseDouble(parts[4].replace("$", "").replace(",", "").trim()));

                    transactions.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading transactions file: " + e.getMessage());
        }
transactions.sort(
        Comparator.comparing(Transaction::getDate)
                .thenComparing(Transaction::getTime)
                .reversed()
);
        return transactions;
    }

}
