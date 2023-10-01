package data;

import java.util.Scanner;

public final class Utils {
    public static final Scanner stdinScanner = new Scanner(System.in);

    /**
     * @param prompt The prompt to display to the user.
     * @return The line read from the user.
     */
    public static String readLine(String prompt) {
        System.out.print(prompt);
        return stdinScanner.nextLine();
    }

    public static String readLine() {
        return readLine("");
    }
}
