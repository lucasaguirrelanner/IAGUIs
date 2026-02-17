public class PASSWORDHELPER {

    public static boolean isPasswordComplex(String password) {
        if (password == null) return false;

        return isPasswordAtLeastEightCharactersLong(password) &&
                doesPasswordContainUpperCaseLetters(password) &&
                doesPasswordContainLowerCaseLetters(password) &&
                doesPasswordContainNumbers(password) &&
                doesPasswordContainSpecialCharacters(password);
    }

    //At least one uppercase letter
    public static boolean doesPasswordContainUpperCaseLetters(String password) {
        return password.matches(".*[A-Z].*");
    }

    public static boolean doesPasswordContainLowerCaseLetters(String password) {
        return password.matches(".*[a-z].*");
    }


    //At least 1 number in the password
    public static boolean doesPasswordContainNumbers(String password) {
        return password.matches(".*[0-9].*");
    }

    //At least one special character
    public static boolean doesPasswordContainSpecialCharacters(String password) {
        return password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    public static boolean isPasswordAtLeastEightCharactersLong(String password) {
        return password.length() >= 8;
    }

   //Error messages that signal the reason as to why the password does not pass the format check.
    public static String getPasswordRequirements(String password) {
        if (!isPasswordAtLeastEightCharactersLong(password)) return "Password must be at least 8 characters.";
        if (!doesPasswordContainUpperCaseLetters(password)) return "Include at least one uppercase letter.";
        if (!doesPasswordContainLowerCaseLetters(password)) return "Include at least one lowercase letter.";
        if (!doesPasswordContainNumbers(password)) return "Include at least one number.";
        if (!doesPasswordContainSpecialCharacters(password)) return "Include at least one special character (!@#$).";
        return "OK";
    }
}