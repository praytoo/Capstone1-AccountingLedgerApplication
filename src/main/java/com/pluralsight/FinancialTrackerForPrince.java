package com.pluralsight;

import java.util.Scanner;

public class FinancialTrackerForPrince {
    static Scanner scanner = new Scanner(System.in);

    //Handles ledger display, reports, and filtering logic
    public static void main(String[] args) {
        boolean endProgram = false;
        while(!endProgram) {
            endProgram = showHomeScreen();
        }}
    public static boolean showHomeScreen(){
        String options = """
                WELCOME PRINCE
                Would you like to...
                D) Add deposit
                P) Make Payment
                L) Ledger
                X) Exit
                """;
        switch (getLetterChoice(options)) {
            case "D":
                TransactionFileManager.addDeposit();
                break;
            case "P":
                TransactionFileManager.addPayment();
                break;
            case "L":
                Ledger.displayLedger();
                break;
            case "X":
                return true;
            default:
                System.out.println("Prince, that's not an option.");
                break;
        }
        return false;
    }

    public static String getLetterChoice(String options) {
        System.out.println(options);
        String choice = scanner.nextLine();
        return choice;
    }
}
