package data;

import business.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/** DBQuery Class
    Class serves as interface with carpool site database.
    */

public class DBQuery {

    /** Blank constructor uses default values for database, etc. */

    public DBQuery(){}

    // ------[TEST VALIDATION]------ //
    /*
    public static void main(String[] args) {
        if(isValidUserName(""))
            System.out.println("USER");
        if(isValidName(""))
            System.out.println("NAME");
        if(isValidPassword(""))
            System.out.println("PASS");
        if(isValidGender(""))
            System.out.println("GENDER");
        if(isValidUserType(""))
            System.out.println("USERTYPE");
        if(isValidID(""))
            System.out.println("ID");
        if(isValidEmail(""))
            System.out.println("EMAIL");
        if(isValidPhone(""))
            System.out.println("PHONE");
    }
     */

    public static boolean isValidUserName(String s) {
        if(s == null || s.isEmpty() || s.length() > 45 || !validUsername(s))
            return false;
        return true;
    }

    public static boolean isValidPassword(String s) {
        if(s == null || s.isEmpty() || s.length() > 45 || s.length() < 5)
            return false;
        return true;
    }

    public static boolean isValidID(String s) {
        if(s == null || s.isEmpty() || s.length() > 10)
            return false;
        else
            for(int j = 0; j < s.length(); j++)
                if(!Character.isDigit(s.charAt(j)))
                    return false;
        return true;
    }

    public static boolean isValidName(String s) {
        if(s == null || s.isEmpty() || s.length() > 45 || !containsLetters(s))
            return false;
        return true;
    }

    public static boolean isValidEmail(String s) {
        if(s == null || s.isEmpty() || s.length() > 45)
            return false;
        return true;
    }

    public static boolean isValidPhone(String s) {
        if(s == null || s.isEmpty() || s.length() != 10)
            return false;
        else
            for(int i = 0; i < s.length(); i++)
                if(!Character.isDigit(s.charAt(i)))
                    return false;
        return true;
    }

    public static boolean isValidUserType(Object o) {
        if(o == null)
            return false;
        if(o.equals(UserType.Both) || o.equals(UserType.Driver)
                || o.equals(UserType.Passenger))
            return true;
        return false;
    }

    public static boolean isValidGender(Object o) {
        if(o == null)
            return false;
        if(o.equals(Gender.Female) || o.equals(Gender.Male)
                || o.equals(Gender.Other))
            return true;
        return false;
    }

    public static boolean containsLetters(String s) {
        for(int i = 0; i < s.length(); i++) {
            if(!Character.isLetter(s.charAt(i)))
                return false;
        }
        return true;
    }

    /** Checks database to see if a Username has already been taken
        @param name the Username being validated
        @return true if the Username does not already exist, false if exists
        */

    public static boolean validUsername(String name) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = ("CALL getValidUsername ('" + name + "');");

        try {
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next())
                return false;

            rs.close();
            ps.close();
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(0);
            return false;
        }
        finally {
            pool.freeConnection(connection);
        }
        return true;
    }

    // ------[INSERTION FUNCTIONS]------ //

    /** Add a piece of feedback to the database.
        @param feedback the feedback needing to be added.
        @return true if successfully added, false otherwise
        */

    public static boolean addFeedback(Feedback feedback) {
        return addFeedback(feedback.getCommenter_id(),
                feedback.getReceiver_id(),
                feedback.getGood_driver(),
                feedback.getOn_time(),
                feedback.getReliable());
    }

    /** Add a piece of feedback to the database.
        @param commenter_id the SJSU id # of the student leaving the feedback
        @param receiver_id the SJSU id of the student being evaluated
        @param good_driver the receiver's good driving score
        @param on_time the receiver's timeliness score
        @param reliable the receiver's reliability score
        @return true if successfully added, false otherwise
        */

    public static boolean addFeedback(String commenter_id, String receiver_id,
            int good_driver, int on_time, int reliable) {
        if (commenter_id.equals(receiver_id) || !isValidID(commenter_id)
                || !isValidID(receiver_id))
            return false; // sure, I love to give myself good ratings... <-- LOL

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "CALL addFeedback ('"
                + commenter_id + "','" + receiver_id + "'," + good_driver + ","
                + reliable + "," + on_time + ");";

        try {
            ps = connection.prepareStatement(query);
            ps.execute();
            ps.close();
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
        finally {
            pool.freeConnection(connection);
        }
    }

    /** Adds a user to the database.
        @param user the user needing to be added to the database
        @return true if adding the user to db succeeds, false otherwise
        */

    public static boolean addUser(User user) {
        if(isValidUserName(user.getUSER_NAME()) && isValidPassword(user.getPassword())
                && isValidName(user.getFirstName()) && isValidName(user.getLastName())
                && isValidGender(user.getGender()) && isValidUserType(user.getUserType())
                && isValidEmail(user.getEmail()) && isValidPhone(user.getPhone())
                && isValidID(user.getSJSU_ID())) {

            ConnectionPool pool = ConnectionPool.getInstance();
            Connection connection = pool.getConnection();
            PreparedStatement ps = null;

            String query = "CALL addUser ('" + user.getUSER_NAME() + "','"
                    + user.getPassword() + "','" + user.getSJSU_ID() + "','"
                    + user.getFirstName() + "','" + user.getLastName() + "','"
                    + user.getGender() + "','" + user.getUserType() + "','"
                    + user.getEmail() + "','" + user.getPhone() + "');";

            try {
                ps = connection.prepareStatement(query);
                ps.execute();
                ps.close();
                return true;
            }
            catch (Exception e) {
                System.out.println(e);
                System.exit(0);
                return false;
            }
            finally {
                pool.freeConnection(connection);
            }
        }
        else
            return false;
    }

    /** Updates a piece of feedback in the database.
        @param commenter_id the SJSU id # of the student leaving the feedback
        @param receiver_id the SJSU id of the student being evaluated
        @param good_driver the receiver's good driving score
        @param on_time the receiver's timeliness score
        @param reliable the receiver's reliability score
        @return true if successfully updated, false otherwise
        */

    public static boolean updateFeedback(String commenter_id, String receiver_id,
            int good_driver, int on_time, int reliable) {
        if (commenter_id.equals(receiver_id) || isValidID(commenter_id)
                || isValidID(receiver_id))
            return false; // no self-commentary

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "CALL updateFeedback ('" + commenter_id + "','"
                + receiver_id + "'," + good_driver + "," + reliable
                + "," + on_time +");";

        try {
            ps = connection.prepareStatement(query);
            ps.execute();
            ps.close();
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
        finally {
            pool.freeConnection(connection);
        }
    }

    // ------[CHECK DATABASE MEMBER FUNCTIONS]------ //

    /** Returns a User object generated from the database Student table,
        taking a username and a password as arguments.
        @param username the username of the desired User
        @param password the password of the desired User for this site
        @return the appropriate User, or null if not found
        */

    public static User getUser(String username, String password) {
        User foundUser = null;

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = ("CALL getUser ('" + username + "','"
                    + password + "');");


        try {
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                foundUser = new User(
                    rs.getString("First_Name"),
                    rs.getString("Last_Name"),
                    rs.getString("Username"),
                    rs.getString("EMail"),
                    rs.getString("Password"),
                    rs.getString("Student_ID"),
                    Gender.valueOf(rs.getString("Gender")),
                    UserType.valueOf(rs.getString("User_Type")),
                    rs.getString("Phone"));

                fillFeedback(foundUser);
                fillCarpools(foundUser);
            }

            ps.close(); // comes here if no user found
            return foundUser;
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(0);
            return foundUser;
        }
        finally {
            pool.freeConnection(connection);
        }
    }

    /** Returns a User object generated from the database Student table,
        taking an SJSU id number.
        @param SJSU_ID the id number of the desired user
        @return the appropriate User, or null if not found
        */

    public static User getUserFromID(String SJSU_ID) {
        if(!isValidID(SJSU_ID))
            return null;

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "CALL getUserFromID ('" + SJSU_ID + "');";

        try {
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User foundUser = new User(
                    rs.getString("First_Name"),
                    rs.getString("Last_Name"),
                    rs.getString("Username"),
                    rs.getString("EMail"),
                    rs.getString("Password"),
                    SJSU_ID,
                    Gender.valueOf(rs.getString("Gender")),
                    UserType.valueOf(rs.getString("User_type")),
                    rs.getString("Phone"));
                ps.close();

                fillFeedback(foundUser);
                fillCarpools(foundUser);
                return foundUser;
            }

            ps.close(); // comes here if no user found
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
        finally {
            pool.freeConnection(connection);
        }

        return null; // default: return null User
    }

    /** Returns a User object generated from the database Student table,
        taking an SJSU id number.
        @param userName the username of the desired user
        @return the appropriate User, or null if not found
        */

    public static User getUserFromUserName(String userName) {
        if(!isValidName(userName))
            return null;

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        String query = ("SELECT * from user where Username='" + userName + "';");

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                User foundUser = new User(
                    rs.getString("First_Name"),
                    rs.getString("Last_Name"),
                    userName,
                    rs.getString("EMail"),
                    rs.getString("Password"),
                    rs.getString("Student_ID"),
                    Gender.valueOf(rs.getString("Gender")),
                    UserType.valueOf(rs.getString("User_type")),
                    rs.getString("Phone"));

                fillFeedback(foundUser);
                fillCarpools(foundUser);
                statement.close();
                return foundUser;
            }

            statement.close(); // comes here if no user found
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
        finally {
            pool.freeConnection(connection);
        }

        return null; // default: return null User
    }

    public static ArrayList<User> getCarpoolUsers(int capool_ID) {
        ArrayList<User> users = new ArrayList<User>();

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "CALL getCarpoolUsers (" + capool_ID + ");";

        try {
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next())
                users.add(getUserFromUserName(rs.getString("Username")));

            rs.close();
            ps.close();
        }
        catch(Exception e) {
            System.out.println(e);
            System.exit(0);
        }
        finally {
            pool.freeConnection(connection);
        }

        return users;
    }

/**
    public static boolean[] getDaysOfWeek(int capool_ID) {

        boolean[] days_bool = new boolean[Carpool.DAYS_IN_WEEK];

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "CALL getDaysOfWeek (" + capool_ID + ");";

        try {
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                char[] days = rs.getString("Day").toCharArray();

                for (int i = 0; i < days.length; i++)
                    days_bool[i]  = (days[i] != '-' && days[i] != ' ');
            }
            rs.close();
            ps.close();
        }
        catch(Exception e) {
            System.out.println(e);
            System.exit(0);
        }
        finally {
            pool.freeConnection(connection);
        }

        return days_bool;
    }
 */
   // ------[USER-OBJECT CONSTRUCTION FUNCTIONS]------ //

    /** Fills out a user object's carpool field using db info. */

    public static void fillCarpools(User user) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "CALL getUsersCarpools (" + user.getSJSU_ID() + ");";

        try {
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            user.clearCarpools();

            while (rs.next()) {
                char[] days = rs.getString("Day").toCharArray();
                boolean[] days_bool = new boolean[Carpool.DAYS_IN_WEEK];
                for (int i = 0; i < days.length; i++)
                    days_bool[i]  = (days[i] != '-' && days[i] != ' ');
                java.sql.Date start = rs.getDate("Start");
                java.sql.Date end   = rs.getDate("End");

                // note: not all receiver data is made available to commenter
                Carpool c = new Carpool(
                            rs.getBoolean("To_campus"),
                            rs.getBoolean("One_time"),
                            days_bool,
                            start,
                            end,
                            rs.getTime("Campus_Time"),
                            getCarpoolUsers(rs.getInt("Carpool_ID")),
                            new Location(rs.getString("Street_intersection"),
                                         rs.getString("City"),
                                         Integer.parseInt(rs.getString("Zip_code")))
                            );
                user.addCarpool(c);
            }

            rs.close();
            ps.close();
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
        finally {
            pool.freeConnection(connection);
        }
    }

    /** Fills out a user object's feedback field using db info. */

    public static void fillFeedback(User user) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "CALL fillFeedback ('" + user.getSJSU_ID() + "');";

        try {
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            user.clearFeedback();

            while (rs.next()) {
                // note: not all receiver data is made available to commenter
                Feedback f = new Feedback(user.getSJSU_ID(),
                    rs.getString("Student_ID"),
                    rs.getString("Username"),
                    rs.getInt("Good_Driver"),
                    rs.getInt("On_Time"),
                    rs.getInt("Reliable"));
                user.addFeedback(f);
            }

            ps.close();
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
        finally {
            pool.freeConnection(connection);
        }
    }

    public static ArrayList<Carpool> searchCarpools(String startDate, String endDate,
            String day, boolean toCampus, String arrivalTime) {
        ArrayList<Carpool> carpools = new ArrayList<Carpool>();

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query;
        if (toCampus)
            query = "CALL getCarpoolsToSchool ('" + day + "', '" + startDate +"', '"
                + endDate + "', " + toCampus + ", '" + arrivalTime + "');";
        else query = "CALL getCarpoolsFromSchool ('" + day + "', '" + startDate +"', '"
                + endDate + "', " + toCampus + ", '" + arrivalTime + "');";

        try {
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                char[] days = rs.getString("Day").toCharArray();
                boolean[] days_bool = new boolean[Carpool.DAYS_IN_WEEK];
                for (int i = 0; i < Carpool.DAYS_IN_WEEK; i++)
                    days_bool[i]  = (days[i] != '-' && days[i] != ' ');
                boolean one_time = (days[Carpool.DAYS_IN_WEEK] != '-'
                        && days[Carpool.DAYS_IN_WEEK] != ' '); // IS IT THE END OF ALL? T__T

                Location location = new Location(rs.getString("Street"),
                                                 rs.getString("City"),
                                                 rs.getInt("Zip"));

                Carpool c = new Carpool(rs.getBoolean("To_Campus"),
                                        one_time,
                                        days_bool,
                                        rs.getDate("Start_Date"),
                                        rs.getDate("End_Date"),
                                        rs.getTime("Arrival_Time"),
                                        getCarpoolUsers(rs.getInt("Carpool_ID")),
                                        location);
                carpools.add(c);
            }

            rs.close();
            ps.close();
        }
        catch(Exception e) {
            System.out.println(e);
            System.exit(0);
        }
        finally {
            pool.freeConnection(connection);
        }

        return carpools;
    }

    // ------[MESSAGING CODE]------ //

    /** Saves a message to the database.
        @param sendername username of the sender
        @param receivername username of the receiver
        @param message the text of the message
        */

    public static void saveMessage(String sendername,
            String receivername, String message)
    {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        String query = ("INSERT INTO message (sender_name, receiver_name, "
                + "text, date) VALUES ('" + sendername + "', '"
                + receivername + "', '" + message + "', curdate());");

        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
        finally { pool.freeConnection(connection); }
    }

    /** Retrieves messages from the database.
        @param username username of the person
        @return an HTML string with all of username's messages
        */

    public static String getMessagesFor(String username)
    {
        String msg = "";

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        String query = ("SELECT * FROM message WHERE "
               + "receiver_name='" + username + "';");

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            boolean foundOne = false;

            while (rs.next()) {
                foundOne = true;
                // note: not all receiver data is made available to commenter
                msg += "<div><p><b>From: </b>" + rs.getString("sender_name")
                        + "</p><p><b>On: </b>" + rs.getDate("date") + "</p><p>"
                        + rs.getString("text") + "</p><p>&nbsp;</p></div>";
            }

            if (!foundOne)
                msg += "<div><p><b>No recent messages. </b></p></div>";
            statement.close();
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
        finally { pool.freeConnection(connection); }

        return msg;
    }
}
