package com.cmu.heinz.resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author owagoner
 */
public class KeyBoardInput {

    public String getKeyboardInput(String prompt) {
        String input = "";
        System.out.println(prompt);
        try {
            InputStreamReader reader = new InputStreamReader(System.in);
            BufferedReader buffer = new BufferedReader(reader);
            input = buffer.readLine();
        } catch (Exception e) {
        }
        return input;
    }

    public int getInteger(boolean validateInput, int defaultResult, int minAllowableResult, int maxAllowableResult, String prompt) {
        String inputString = "";
        int result = defaultResult;
        boolean entryAccepted = false;
        while (!entryAccepted) {
            result = defaultResult;
            entryAccepted = true;
            inputString = getKeyboardInput(prompt);
            if (inputString.length() > 0) {
                try {
                    result = Integer.parseInt(inputString);
                } catch (Exception e) {
                    entryAccepted = false;
                    System.out.println("Invalid entry...");
                }
            }
            if (entryAccepted && validateInput) {
                if ((result != defaultResult) && ((result < minAllowableResult) || (result > maxAllowableResult))) {
                    entryAccepted = false;
                    System.out.println("Invalid entry. Allowable range is " + minAllowableResult + "..." + maxAllowableResult + " (default = " + defaultResult + ").");
                }
            }
        }
        return result;
    }

    public double getDouble(boolean validateInput, double defaultResult, double minInput, double maxInput, String prompt) {
        String inputString = "";
        double result = defaultResult;
        boolean entryAccepted = false;
        while (!entryAccepted) {
            result = defaultResult;
            entryAccepted = true;
            inputString = getKeyboardInput(prompt);
            if (inputString.length() > 0) {
                try {
                    result = Double.parseDouble(inputString);
                } catch (Exception e) {
                    entryAccepted = false;
                    System.out.println("Invalid entry...");
                }
            }
            if (entryAccepted && validateInput) {
                if ((result != defaultResult) && ((result < minInput) || (result > maxInput))) {
                    entryAccepted = false;
                    System.out.println("Invalid entry. Allowable range is " + minInput + "..." + maxInput + " (default = " + defaultResult + ").");
                }
            }
        }
        return result;
    }

    public String getString(String defaultResult, String prompt) {
        String result = getKeyboardInput(prompt);
        if (result.length() == 0) {
            result = defaultResult;
        }
        return result;
    }
}
