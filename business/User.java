package business;

import java.io.Serializable;
import java.util.ArrayList;

/** User Class
    Represents a student attending SJSU.
    */

public class User implements Serializable
{
    /** Default constructor. */

    public User()
    {
        this.gender = Gender.Other;
        this.userType = UserType.Passenger;
        carpools = new ArrayList<Carpool>();
        feedback = new ArrayList<Feedback>();
    }

    /** Full constructor minus carpools and feedback.
        @param firstName first name of the student
        @param lastName surname of the student
        @param USER_NAME username of the student
        @param email e-mail of the student
        @param password password of the student for the carpool site
        @param SJSU_ID sjsu id of the student
        @param gender gender of the student
        @param userType whether the user is a driver/passenger/both
        @param phone phone number of the student
        */

    public User(String firstName, String lastName, String USER_NAME,
            String email, String password, String SJSU_ID, Gender gender,
            UserType userType, String phone)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.USER_NAME = USER_NAME;
        this.email = email;
        this.password = password;
        this.SJSU_ID = SJSU_ID;
        this.gender = gender;
        this.userType = userType;
        this.phone = phone;
        carpools = new ArrayList<Carpool>();
        feedback = new ArrayList<Feedback>();
    }

    // ------[MUTATORS]------ //

    /** Sets the student's first name.
        @param firstName the first name of the student
        */

    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** Sets the student's surname.
        @param lastName the surname of the student
        */

    public void setLastName(String lastName) { this.lastName = lastName; }

    /** Sets the student's e-mail address.
        @param email the student's e-mail address
        */

    public void setEmailAddress(String email) { this.email = email; }

    /** Sets the student's password
        @param password the student's new password
        */

    public void setPassword(String password) { this.password = password; }

    /** Sets the student's gender
        @param gender the student's gender
        */

    public void setGender(Gender gender) { this.gender = gender; }

    /** Sets the student's user type (driver/passenger/both)
        @param userType the student's user type
        */

    public void setUserType(UserType userType) { this.userType = userType; }

    /** Sets the student's phone number
        @param phone the student's phone number
        */

    public void setPhone(String phone) { this.phone = phone; }

    // ------[MEMBER FUNCTIONS]------ //

    /** Add a carpool to this user.
        @param c the new carpool to which this student is participating
        */

    public void addCarpool(Carpool c)
    {
        if (c != null)
            carpools.add(c);
    }

    /** Add feedback on this user. (Does not persist to db.)
        @param f the user's new feedback.
        */

    public void addFeedback(Feedback f)
    {
        if (f != null)
            feedback.add(f);
    }

    /** Clear all data. */

    public void clear()
    {
        firstName = "";
        lastName = "";
        USER_NAME = "";
        email = "";
        password = "";
        SJSU_ID = "";
        gender = Gender.Other;
        userType = UserType.Passenger;
        phone = "";
        carpools.clear();
        feedback.clear();
    }

    /** Clears the carpools field. */

    public void clearCarpools() { carpools.clear(); }

    /** Clears the feedback field. */

    public void clearFeedback() { feedback.clear(); }

    /** Check if a user has left feedback on another user already.
        @param otherUserID the SJSU id # of other user in question
        @return true if the feedback field contains one for otherUser
        */

    public boolean hasLeftFeedbackOn(String otherUserID)
    {
        for (Feedback f : feedback)
            if (f.getReceiver_id().equals(otherUserID))
                return true;
        return false;
    }

    /** Remove a carpool from this user.
        @param c the carpool in which this user is no longer participating
        */

    public void removeCarpool(Carpool c)
    {
        if (c != null)
            carpools.remove(c);
    }

    /** Remove a piece of feedback from this user. (Does not update db.)
        @param f the piece of feedback to remove from this user
        */

    public void removeFeedback(Feedback f)
    {
        if (f != null)
            feedback.remove(f);
    }

    /** Updates a piece of feedback left by this user.
        @param receiver_id the id # of the feedback receiver
        @param good_driver the new good driving score
        @param on_time the new timeliness score
        @param reliable the new reliability score
        @return true if operation is successful, false otherwise
        */

    public boolean updateFeedback(String receiver_id, int good_driver,
            int on_time, int reliable)
    {
        for (Feedback f : feedback)
            if (f.getReceiver_id().equals(receiver_id))
            {
                f.setGood_driver(good_driver);
                f.setOn_time(on_time);
                f.setReliable(reliable);
                return true;
            }

        return false;
    }

    // ------[ACCESSORS]------ //

    /** Returns the user's first name.
        @return the user's first name
        */

    public String getFirstName() { return firstName; }

    /** Returns the user's surname.
        @return the user's surname
        */

    public String getLastName() { return lastName; }

    /** Returns the user's username.
        @return the user's username
        */

    public String getUSER_NAME() { return USER_NAME; }

    /** Returns the user's e-mail address.
        @return the user's e-mail address
        */

    public String getEmail() { return email; }

    /** Returns the user's SJSU id number.
        @return the user's SJSU id number
        */

    public String getSJSU_ID() { return SJSU_ID; }

    /** Returns the user's password.
        @return the user's password
        */

    public String getPassword() { return password; }

    /** Returns the user's gender.
        @return the user's gender
        */

    public Gender getGender() { return gender; }

    /** Returns the user's user type (driver/passenger/both).
        @return the user's user type
        */

    public UserType getUserType() { return userType; }

    /** Returns the user's phone number.
        @return the user's phone number
        */

    public String getPhone() { return phone; }

    /** Returns the user's carpools.
        @return the user's carpools
        */

    public ArrayList<Carpool> getCarpools() { return carpools; }

    /** Returns the feedback that this user has received.
        @return the feedback that this user has received
        */

    public ArrayList<Feedback> getFeedback() { return feedback; }

    // ------[MEMBER VARIABLES]------ //

    /** the user's first name */
    private String firstName;
    /** the user's surname */
    private String lastName;
    /** the user's username */
    private String USER_NAME;
    /** the user's e-mail address */
    private String email;
    /** the user's password */
    private String password;
    /** the user's SJSU id number */
    private String SJSU_ID;
    /** the user's gender */
    private Gender gender;
    /** the user's user type (driver/passenger/both) */
    private UserType userType;
    /** the user's phone number */
    private String phone;
    /** the carpools in which this user is participating */
    private ArrayList<Carpool> carpools;
    /** the feedback which this user has received */
    private ArrayList<Feedback> feedback;
}
