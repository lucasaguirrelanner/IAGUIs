public class PASSWORDHELPER{

    //the boolean will encompass all  password complexity requirements established:

    public static boolean isPasswordComplex(String password) {
        if (password == null) return false;

        return isPasswordAtLeastEightCharactersLong(password) &&
                doesPasswordContainUpperCaseLetters(password) &&
                doesPasswordContainLowerCaseLetters(password) &&
                doesPasswordContainNumbers(password) &&
                doesPasswordContainSpecialCharacters(password);

    }

//checks that complexity meets format requirements:

    public static boolean doesPasswordContainUpperCaseLetters(String password){
        return password.matches(".*[A-Z].*");
    }
    public static boolean doesPasswordContainLowerCaseLetters(String password){
        return password.matches(".*[a-z].*");
    }

    public static boolean doesPasswordContainNumbers(String password){
        return password.matches(".*[0-9].*");
    }

    public static boolean doesPasswordContainSpecialCharacters(String password){
        return password.matches(".*[!@#$%^&*(),.?\":{}|<>|.]*");
    }

    public static boolean isPasswordAtLeastEightCharactersLong(String password){
        return password.length() >= 8;
    }

//this string helps display error messages that orient the user in case that their password does not meet one of the requirements:

    public static String getPasswordRequirements(String password) {
        if (!isPasswordAtLeastEightCharactersLong(password)) return
                "Password must be at least 8 characters.";
        if (!doesPasswordContainUpperCaseLetters(password)) return
                "Include at least 1 uppercase letter.";
        if (!doesPasswordContainLowerCaseLetters(password)) return
                "Include at least one lowercase letter.";
        if (!doesPasswordContainNumbers(password)) return
                "Include at least one number.";
        if (!doesPasswordContainSpecialCharacters(password)) return
                "Include at least one special character (!@#$).";
        return "OK";

    }

}
