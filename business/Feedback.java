package business;

import java.io.Serializable;

/** Feedback Class
    Represents a feedback object containing feedback from
    one user upon another covering several categories.
    */

public class Feedback implements Serializable
{
    /** Default constructor. */

    @SuppressWarnings("empty-statement")
    public Feedback() { ; }

    /** Full constructor.
        @param commenter the user leaving the feedback
        @param receiver the user being evaluated
        @param good_driver the good driver score
        @param on_time the timeliness score
        @param reliable the reliability score
        */

    public Feedback(String commenter_id, String receiver_id, String receiver_name,
            int good_driver, int on_time, int reliable)
    {
        setCommenter_id(commenter_id);
        setReceiver_id(receiver_id);
        setReceiver_name(receiver_name);
        setGood_driver(good_driver);
        setOn_time(on_time);
        setReliable(reliable);
    }

    // ------[ACCESSORS]------ //

    /** Returns the user leaving the feedback.
        @return the user leaving the feedback
        */

    public String getCommenter_id() { return commenter_id; }

    /** Returns the good driver feedback score.
        @return the good driver feedback score
        */

    public int getGood_driver() { return good_driver; }

    /** Returns the timeliness feedback score.
        @return the timeliness feedback score
        */

    public int getOn_time() { return on_time; }

    /** Returns the user being evaluated.
        @return the user being evaluated
        */

    public String getReceiver_id() { return receiver_id; }

    /** Returns the username of the user being evaluated.
        @return the username of the user being evaluated
        */
    public String getReceiver_name() { return receiver_name; }

    /** Returns the reliability feedback score.
        @return the reliability feedback score
        */

    public int getReliable() { return reliable; }

    // ------[MUTATORS]------ //

    /** Sets the commenter.
        @param commenter_id the id of the user leaving the feedback
        @return true if successful
        */

    public boolean setCommenter_id(String commenter_id)
    {
        this.commenter_id = commenter_id;
        return true;
    }

    /** Sets good driver score
        @param good_driver the good driver score
        @return true if successful
        */

    public boolean setGood_driver(int good_driver)
    {
        if (good_driver < MIN_SCORE)
            good_driver = MIN_SCORE;
        if (good_driver > MAX_SCORE)
            good_driver = MAX_SCORE;
        this.good_driver = good_driver;
        return true;
    }

    /** Sets timeliness score
        @param on_time the timeliness score
        @return true if successful
        */

    public boolean setOn_time(int on_time)
    {
        if (on_time < MIN_SCORE)
            on_time = MIN_SCORE;
        if (on_time > MAX_SCORE)
            on_time = MAX_SCORE;
        this.on_time = on_time;
        return true;
    }

    /** Sets the id of the user being evaluated.
        @param receiver_id the id of the user being evaluated
        @return true if successful
        */

    public boolean setReceiver_id(String receiver_id)
    {
        this.receiver_id = receiver_id;
        return true;
    }

    /** Sets the username of the user being evaluated.
        @param receiver_name the username of the user being evaluated
        @return true if successful
        */

    public boolean setReceiver_name(String receiver_name)
    {
        if (receiver_name == null)
            this.receiver_name = "";
        else this.receiver_name = receiver_name;
        return true;
    }

    /** Sets reliability score
        @param reliable the reliability score
        @return true if successful
        */

    public boolean setReliable(int reliable)
    {
        if (reliable < MIN_SCORE)
            reliable = MIN_SCORE;
        if (reliable > MAX_SCORE)
            reliable = MAX_SCORE;
        this.reliable = reliable;
        return true;
    }

    // ------[MEMBER VARIABLES]------ //

    /** constant: max feedback score cap */
    public static final int MAX_SCORE = 5;
    /** constant: min feedback score cap */
    public static final int MIN_SCORE = 1;

    /** the user leaving the feedback */
    private String commenter_id;
    /** if of the user being evaluated */
    private String receiver_id;
    /** username of the user being evaluated */
    private String receiver_name;
    /** feedback score: good driver */
    private int good_driver;
    /** feedback score: reliability */
    private int reliable;
    /** feedback score: timeliness */
    private int on_time;
}
