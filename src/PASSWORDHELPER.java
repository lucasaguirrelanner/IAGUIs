public class PASSWORDHELPER {

    /**
     * Main validation method.
     * Checks all complexity requirements.
     */
    public static boolean isPasswordComplex(String password) {
        if (password == null) return false;

        return isPasswordAtLeastEightCharactersLong(password) &&
                doesPasswordContainUpperCaseLetters(password) &&
                doesPasswordContainLowerCaseLetters(password) &&
                doesPasswordContainNumbers(password) &&
                doesPasswordContainSpecialCharacters(password);
    }

    // Regex Fixed: ".*[A-Z].*" looks for at least one Uppercase
    public static boolean doesPasswordContainUpperCaseLetters(String password) {
        return password.matches(".*[A-Z].*");
    }

    public static boolean doesPasswordContainLowerCaseLetters(String password) {
        return password.matches(".*[a-z].*");
    }

    // FIXED: Your previous regex ".*[0-9]*." allowed zero numbers. 
    // This requires at least one digit.
    public static boolean doesPasswordContainNumbers(String password) {
        return password.matches(".*[0-9].*");
    }

    // FIXED: This requires at least one special character from the set.
    public static boolean doesPasswordContainSpecialCharacters(String password) {
        return password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    public static boolean isPasswordAtLeastEightCharactersLong(String password) {
        return password.length() >= 8;
    }

    /**
     * UX ENHANCEMENT: Returns a string explaining what is missing.
     * This helps meet the "User-Friendly" aspect of your platform.
     */
    public static String getPasswordRequirements(String password) {
        if (!isPasswordAtLeastEightCharactersLong(password)) return "Password must be at least 8 characters.";
        if (!doesPasswordContainUpperCaseLetters(password)) return "Include at least one uppercase letter.";
        if (!doesPasswordContainLowerCaseLetters(password)) return "Include at least one lowercase letter.";
        if (!doesPasswordContainNumbers(password)) return "Include at least one number.";
        if (!doesPasswordContainSpecialCharacters(password)) return "Include at least one special character (!@#$).";
        return "OK";
    }
}