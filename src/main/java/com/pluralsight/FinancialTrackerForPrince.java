package com.pluralsight;

import java.util.Scanner;

import static com.pluralsight.Ledger.showLedger;
import static com.pluralsight.TransactionFileManager.addDeposit;
import static com.pluralsight.TransactionFileManager.addPayment;

public class FinancialTrackerForPrince {
    //Handles home screen display
    static Scanner scanner = new Scanner(System.in);

    //loops home screen
    public static void main(String[] args) {
        boolean endProgram = false;
        while (!endProgram) {
            endProgram = showHomeScreen();
        }
    }

    // home screen menu
    public static boolean showHomeScreen() {
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

    //handles case choice
    public static String getLetterChoice(String options) {
        System.out.println(options);
        String choice = scanner.nextLine();
        return choice.toUpperCase();
    }
}
