package com.pluralsight;

import java.util.Scanner;

public class Ledger {
    static Scanner scanner = new Scanner(System.in);

    //Handles ledger display, reports, and filtering logic
    public static void main(String[] args) {
        boolean endProgram = false;
        while(!endProgram) {
            endProgram = showHomeScreen();
    }}
    public static boolean showHomeScreen(){
        String options = """
                1) Add deposit
                2) Payment
                3) Ledger
                4) Exit
                """;
            switch (getNumericChoice(options)) {
                case 1:
                    TransactionFileManager.addDeposit();
                    break;
                case 2:
                    TransactionFileManager.addPayment();
                    break;
                case 3:
                    Ledger.showLedger();
                    break;
                case 4:
                    return true;
                default:
                    System.out.println("That's not an option.");
                    break;
            }
            return false;
    }

    public static int getNumericChoice(String options) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(options);
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
    public static void showLedger(){

    }
}

