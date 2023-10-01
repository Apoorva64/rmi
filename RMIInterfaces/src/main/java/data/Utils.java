package data;

public final class Utils {
    /**
     * @param prompt The prompt to display to the user.
     * @return The line read from the user.
     */
    public static String readLine(String prompt) {
        System.out.print(prompt);
        return System.console().readLine();
    }

    public static String readLine() {
        return readLine("");
    }
}
