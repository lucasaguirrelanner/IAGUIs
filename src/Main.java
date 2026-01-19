import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main
{
    public static boolean ListUsernamesAndPasswords()
    {
        // 1) List usernames and passwords
        // Database URL, Username, and password
        String url = "jdbc:mysql: //localhost:3306/IA"; //change database name
        String user = "root"; //Change username if necessary
        String password = "Maleo207"; //Change your MySQL password

        //Establish the connection
        try (Connection conn = DriverManager.getConnection(url, user, password))
        {
            System.out.println("Connection successful");
            //run your query
            try
            {
                //prepare an SQL query to retrieve user credentials
                String query = "SELECT * FROM IA.Patient";
                PreparedStatement select_statement;
                select_statement = conn.prepareStatement(query);

                //Execute the query and retrieve the result set
                ResultSet resultSet = select_statement.executeQuery();

                while (resultSet.next())
                {
                    String databaseUsername = resultSet.getString("username");
                    String databasePassword = resultSet.getString("password");

                    System.out.printf("Username is %s\t password is %s\n", databaseUsername, databasePassword);
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        return true;
    }

    public static boolean AddUsernameAndPassword()
    {
        // 2) add usernames and passwords
        // Database URL, Username, and password
        String url = "jdbc:mysql: //localhost:3306/usernamepassword"; //change database name
        String user = "root"; //Change username if necessary
        String password = "Maleo207"; //Change your MySQL password

        Scanner scanner = new Scanner(System.in);

        //ask for new username and password
        System.out.print("Enter username: ");
        String newUsername = scanner.nextLine();

        String newPassword = "";
        String confirmedPassword = "";

        while (isPasswordComplex(newPassword) == false)
        {
            System.out.print("Enter password: ");
            newPassword = scanner.nextLine();

            //check if password meets the requirements
            if (!isPasswordComplex(newPassword))
            {
                System.out.println("Password is not complex enough! Please remember that your password must have numbers, lower and uppercase letters, contain special characters and it must be at least 8 characters long. ");
                continue;
            }

            System.out.println( "Please confirm your password");
            confirmedPassword = scanner.nextLine();

            if (newPassword.equals(confirmedPassword)) {
                break;
            }else{

                System.out.println(" Passwords do not match. Please try again. ");

            }
        }

        try (Connection conn = DriverManager.getConnection(url, user, password))
        {
            System.out.println("Connection successful");

            // Prepare SQL query to insert new user and password
            String insertQuery = "INSERT INTO Patient (username, password) VALUES (?, ?)";
            try (PreparedStatement insertStatement = conn.prepareStatement(insertQuery))
            {
                insertStatement.setString(1, newUsername);
                insertStatement.setString(2, newPassword);

                // Execute the query
                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0)
                {


                    System.out.println("A new user was inserted successfully!");

                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        return true;
    }

    public static boolean ModifyUsernameOrPassword()
    {
        // 3) Modify usernames and/or passwords

        // Database URL, Username, and password
        String url = "jdbc:mysql: //localhost:3306/IA"; //change database name
        String user = "root"; //Change username if necessary
        String password = "Maleo207"; //Change your MySQL password

        Scanner scanner = new Scanner(System.in);

        // Ask the user which username to modify
        System.out.println("Enter the username you want to modify: ");
        String oldUsername = scanner.nextLine();

        // Ask for new username
        System.out.println("Enter new username: ");
        String newUsername = scanner.nextLine();

        // Ask for new password
        System.out.println("Enter new password: ");
        String newPassword = scanner.nextLine();

        // Check if new password is provided and validate its complexity
        if (!newPassword.isEmpty() && !isPasswordComplex(newPassword)) {
            System.out.println("Password is not complex enough!");
        }

        // Establish the connection
        try (Connection conn = DriverManager.getConnection(url, user, password))
        {
            System.out.println("Connection successful");

            // Build the SQL query for updating the user record
            String updateQuery = "UPDATE users SET ";
            boolean updateUsername = !newUsername.isEmpty();
            boolean updatePassword = !newPassword.isEmpty();

            // Aadjust the query based on what the user wants to modify
            if (updateUsername)
            {
                updateQuery += "username = ?";
            }
            if (updatePassword)
            {
                if (updateUsername) updateQuery += ", ";
                updateQuery += "password = ?";
            }
            updateQuery += "WHERE username = ?";

            // Prepare and execute the query
            try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery))
            {
                int paramIndex = 1;
                if (updateUsername)
                {
                    updateStatement.setString(paramIndex++, newUsername); // Set new username
                }
                if (updatePassword)
                {
                    updateStatement.setString(paramIndex++, newPassword); // Set new password
                }

                // Set the condition (old username)
                updateStatement.setString(paramIndex, oldUsername);

                // do the update
                int rowsUpdated = updateStatement.executeUpdate();
                if (rowsUpdated > 0)
                {
                    System.out.println("User information updated successfully!");
                }
                else
                {
                    System.out.println("No user found with the provided username.");
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return true;
    }

    public static boolean DeleteUsernameAndPassword()
    {
        // 4) Delete usernames and passwords
        // Database URL, Username, and password
        String url = "jdbc:mysql://localhost:3306/IA"; //change database name
        String user = "root"; //Change username if necessary
        String password = "Maleo207"; //Change your MySQL password

        Scanner scanner = new Scanner(System.in);


        System.out.print("Enter the username that you wish to delete: ");
        String usernameToDelete = scanner.nextLine();
        System.out.print("Enter the password for this username: ");
        String passwordToVerify = scanner.nextLine();

        // Establish the connection
        try (Connection conn = DriverManager.getConnection(url, user, password))
        {
            System.out.println("Connection successful");

            // Prepare and execute the delete statement
            String deleteQuery = "DELETE FROM IA WHERE username = ? AND password = ?";
            try (PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery))
            {
                deleteStatement.setString(1, usernameToDelete);
                deleteStatement.setString(2, passwordToVerify);

                int rowsDeleted = deleteStatement.executeUpdate();
                if (rowsDeleted > 0)
                {
                    System.out.println("User deleted successfully.");
                }
                else
                {
                    System.out.println("Error occurred while deleting the user.");
                }
                return true; // Exit after successful deletion
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return false;
    }

    // add username and password
    public static boolean doesPasswordContainUpperCaseLetters (String password)
    { //regex patter to each for uppercase letters in a string
        String complexityPattern = ".*[A-Z]*.";
        //Return true if password matches the pattern
        return password.matches (complexityPattern);
    }

    public static boolean doesPasswordContainLowerCaseLetters(String password)
    {
        String complexityPattern = ".*[a-z]*.";
        return password.matches (complexityPattern);
    }

    public static boolean doesPasswordContainNumbers(String password)
    {
        String complexityPattern = ".*[0-9]*.";
        return password.matches (complexityPattern);
    }

    public static boolean doesPasswordContainSpecialCharacters(String password)
    {
        String complexityPattern = ".*[$!%*?&]*.";
        return password.matches (complexityPattern);
    }

    public static boolean isPasswordAtLeastEightCharactersLong(String password)
    {
        String complexityPattern = ".{8,}.";
        return password.matches (complexityPattern);
    }

    public static boolean isPasswordComplex(String password)
    {
        return  doesPasswordContainUpperCaseLetters ( password) &&
                doesPasswordContainLowerCaseLetters( password) &&
                doesPasswordContainNumbers( password) &&
                doesPasswordContainSpecialCharacters( password) &&
                isPasswordAtLeastEightCharactersLong( password);
    }

    public static void main(String[] args)
    {
        //ask for username and password
        Scanner scanner = new Scanner(System.in);
        boolean Continue = true;

        while (Continue == true )
        {
            //what the computer prints when we run it
            System.out.println("\n == Security Check Menu == ");

            System.out.println("Welcome to Security Checker.");
            System.out.println("1. Option 1: List usernames and passwords");
            System.out.println("2. Option 2: Add username and password");
            System.out.println("3. Option 3: Modify username and/or password");
            System.out.println("4. Option 4: Delete username & password");
            System.out.println("5. Exit");
            System.out.print("Please choose an option: ");

            try
            {
                int choice  = scanner.nextInt();

                switch ( choice )
                {
                    case 1: //easy
                        System.out.println("List usernames and passwords.");
                        ListUsernamesAndPasswords();

                        break;
                    case 2: //medium
                        System.out.println("Add names and password.");
                        AddUsernameAndPassword();

                        break;
                    case 3: //hard
                        System.out.println("Modify username and/or password.");
                        ModifyUsernameOrPassword();
                        break;
                    case 4: //impossible
                        System.out.println("Delete username & password.");
                        DeleteUsernameAndPassword();
                        break;
                    case 5: //impossible
                        Continue = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Try again");
                }
            }
            catch (InputMismatchException e)
            {
                //handle errors when they're not an integer
                System.out.println("Error: Please enter a valid integer.");
                scanner.next(); //consume invalid input to avoid infinite loop
                System.out.println();
            }
        }
    }
}