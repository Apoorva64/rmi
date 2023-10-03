package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class IO {


    public static String getLine() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static Date getDateTime() {
        Scanner scanner = new Scanner(System.in);
        Date dateTime = null;

        while (dateTime == null) {
            try {
                String dateTimeString = scanner.nextLine();
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                dateTime = dateTimeFormat.parse(dateTimeString);
            } catch (ParseException e) {
                System.out.println("Date Time Format is invalid. Please make sure to respect the format (yyyy-MM-dd HH:mm).");
            }
        }
        return dateTime;
    }


    public static int choice(
            String prompt,
            List<String> options,
            boolean allowExit
    ) {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice == -1) {
            System.out.println(prompt);
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i));
            }
            if (allowExit) {
                System.out.println("0. Exit");
            }
            System.out.print("Choice: ");
            String input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
                if (choice < 0 || choice > options.size()) {
                    System.out.println("Invalid choice.");
                    choice = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
            }
        }
        return choice - 1; // -1 because we want to return the index
    }

    public static Date readDate(String s) {
        System.out.println(s);
        return getDateTime();
    }

    public static int readInt(String s) {
        System.out.println(s);
        Scanner scanner = new Scanner(System.in);
        int number = -1;
        while (number == -1) {
            String input = scanner.nextLine();
            try {
                number = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
        return number;
    }

    public static String readLine(String s) {
        System.out.println(s);
        return getLine();
    }

    public void print(String message) {
        System.out.println(message);
    }
}
