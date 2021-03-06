package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;

class CurrencySettings {
    private double currencyValue;
    private String currencyIdentifier;

    void setCurrencyValue(double currencyValue) {
        this.currencyValue = currencyValue;
    }

    void setCurrencyIdentifier(String currencyIdentifier) {
        this.currencyIdentifier = currencyIdentifier;
    }

    double getCurrencyValue() {
        return this.currencyValue;
    }

    String getCurrencyIdentifier() {
        return this.currencyIdentifier;
    }
}

class Rates {
    public Double SEK;
}

class CurrencyData {
    public String base;
    public String date;
    public Object rates;
}

public class Main {

    public static double getExchangeRate(String currency) throws IOException {
        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader inputStreamReader = null;

        url = new URL("https://api.fixer.io/latest?symbols=SEK&base=" + currency);

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setReadTimeout(5000);
        connection.setConnectTimeout(5000);
        connection.setInstanceFollowRedirects(false);

        inputStreamReader = new InputStreamReader(connection.getInputStream());
        BufferedReader in = new BufferedReader(inputStreamReader);

        String inputLine;
        StringBuffer content = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();

        Gson g = new Gson();
        CurrencyData currencyData = g.fromJson(String.valueOf(content), CurrencyData.class);
        Rates currencyRates = g.fromJson(String.valueOf(currencyData.rates), Rates.class);

        System.out.println(content.toString());

        double currencyRate = currencyRates.SEK;
        return currencyRate;
    }

    public static void startShoppingHelper(Scanner inputReader, CurrencySettings currency) throws IOException {
        System.out.println("\n----------------------------------- Shopping Helper Functions -----------------------------------");

        if (currency.getCurrencyIdentifier() != null || currency.getCurrencyValue() != 0) {
            System.out.println("\n\nCurrency Properties");
            System.out.println("________________________________________________________");
            if (currency.getCurrencyIdentifier() != "")
                System.out.println("Current currency identifier is: " + currency.getCurrencyIdentifier());

            if (currency.getCurrencyValue() != 0)
                System.out.println("Current currency value is: " + currency.getCurrencyValue());
        }

        System.out.println("\nAlternatives");
        System.out.println("________________________________________________________");
        System.out.println("1: Convert one value from SEK to another currency");
        System.out.println("2: Convert multiple values from SEK to another currency");
        System.out.println("3: Change Currency Settings");

        System.out.print("\nSelect an alternative: ");

        int input = inputReader.nextInt();
        if (input == 0) {
            System.out.println("You have to input a value of either 1 or 2.");
            startShoppingHelper(inputReader, currency);
        }

        switch (input) {
            case 1:
                if (currency.getCurrencyIdentifier() == null || currency.getCurrencyValue() == 0) {
                    System.out.println("Please setup your currency settings before attempting this.");
                    break;
                }

                System.out.print("\nInput sum: ");
                double sum = inputReader.nextDouble();

                double calculation = sum * currency.getCurrencyValue();
                System.out.println("Value: " + calculation);

                break;
            case 2:
                if (currency.getCurrencyIdentifier() == null || currency.getCurrencyValue() == 0) {
                    System.out.println("Please setup your currency settings before attempting this.");
                    break;
                }

                List<Double> sums = new ArrayList<Double>();
                int count = 0;
                double price = 0;

                do {
                    count++;
                    System.out.print("\nInput price " + count + " in " + currency.getCurrencyIdentifier() + " (Negative value to exit and do calculation): ");
                    price = inputReader.nextDouble();

                    if (price >= 0) {
                        sums.add(price);
                    }
                } while (price >= 0);

                calculation = 0;
                double calc = 0;

                for (double currentSum:
                     sums) {
                    calc = currentSum * currency.getCurrencyValue();
                    calculation+=calc;
                }

                System.out.println("\nTotal: " + calculation + " SEK");

                break;

            case 3:
                System.out.print("\nCurrency Identifier (Examples: USD, AUD etc): ");
                String currencyID = inputReader.next();

                System.out.print("Currency value of " + currencyID + " (Input negative value to pull data from online exchange): ");
                double currencyValue = inputReader.nextDouble();

                if (currencyValue < 0) {
                    currencyValue = getExchangeRate(currencyID);
                }

                currency.setCurrencyIdentifier(currencyID);
                currency.setCurrencyValue(currencyValue);

                System.out.println("Currency information has been updated.");
        }

        // Start again
        startShoppingHelper(inputReader, currency);
    }

    public static void main(String[] args) throws IOException {
        final double version = 0.1;
        System.out.println("\n\nWelcome to Shopping Helper v" + version + "!");
        System.out.println("Created by Simon Arledal at 2017.08.31");
        System.out.println("E-mail me at arledal.simon@gmail.com");

        // Instantiate object and start the shopping helper
        Scanner inputReader = new Scanner(System.in);

        // Instantiate new object with currency settings
        CurrencySettings currency = new CurrencySettings();

        startShoppingHelper(inputReader, currency);
    }
}
