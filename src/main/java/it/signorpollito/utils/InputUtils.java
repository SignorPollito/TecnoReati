package it.signorpollito.utils;

import java.util.Scanner;

public class InputUtils {

    public static int requestInteger(Scanner scanner, String message, int min, int max) {
        int number = -1;

        do {
            if(number!=-1) System.out.println("\nNumero inserito non valido!");
            System.out.print(message);

            while(!scanner.hasNextInt()) {
                System.out.println("\nDevi inserire un numero!");
                System.out.print(message);
                scanner.next();
            }

            number = scanner.nextInt();
        } while (number<min || number>max);

        scanner.nextLine(); //For debug
        return number;
    }

    public static int requestInteger(Scanner scanner, String message, int min) {
        return requestInteger(scanner, message, min, Integer.MAX_VALUE);
    }

    public static int requestInteger(Scanner scanner, String message) {
        return requestInteger(scanner, message, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static String requestString(Scanner scanner, String message, int minLenght) {
        String input = null;

        do {
            if(input!=null && input.isBlank()) {
                System.out.println("\nDevi scrivere qualcosa!");

            } else if(input!=null && input.length()<minLenght) {
                System.out.println("\nLa frase inserita è troppo corta!");
            }

            System.out.print(message);

            input = scanner.nextLine();
        } while (input.isBlank() || input.length()<minLenght);

        return input;
    }

    public static String requestString(Scanner scanner, String message) {
        return requestString(scanner, message, 0);
    }

    public static boolean requestYes(Scanner scanner, String message) {
        return requestString(scanner, message, 0).startsWith("y");
    }
}