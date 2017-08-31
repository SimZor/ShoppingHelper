package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void startShoppingHelper(Scanner inputReader) {
        System.out.println("\nShopping Helper Functions -----------------------------------");
        System.out.println("\nWhat do you want to do?");
        System.out.println("1 - Convert one value from SEK to another currency");
        System.out.println("2 - Convert multiple values from SEK to another currency");
        
        int input = inputReader.nextInt();
        if (input == 0) {
            System.out.println("You have to input a value of either 1 or 2.");
            startShoppingHelper(inputReader);
        }

        switch (input) {
            case 1:
                System.out.print("Currency value right now: ");
                double currencyValue = inputReader.nextDouble();

                System.out.print("\nInput sum: ");
                double sum = inputReader.nextDouble();

                double calculation = sum * currencyValue;
                System.out.println("Value: " + calculation);

                break;
            case 2:
                System.out.print("Current value of currency to convert to (From SEK to ANY): ");
                currencyValue = inputReader.nextDouble();

                List<Double> sums = new ArrayList<Double>();
                int count = 0;
                double price = 0;

                do {
                    count++;
                    System.out.print("Input Price in SEK of " + count + " product (Negative value to exit and do calculation): ");
                    price = inputReader.nextDouble();

                    if (price >= 0) {
                        sums.add(price);
                    }
                } while (price >= 0);

                calculation = 0;
                double calc = 0;

                for (double currentSum:
                     sums) {
                    calc = currentSum * currencyValue;
                    calculation+=calc;
                }

                System.out.println("Total: " + calculation);

                break;
        }

        // Start again
        startShoppingHelper(inputReader);
    }

    public static void main(String[] args) {
        final double version = 0.1;
	    System.out.println("Welcome to Shopping Helper " + version + "!");
	    System.out.println("Simon Arledal - 2017.08.31");

	    // Instantiate object and start the shopping helper
        Scanner inputReader = new Scanner(System.in);
        startShoppingHelper(inputReader);
    }
}
