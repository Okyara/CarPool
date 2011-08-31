package business;

import java.io.Serializable;
import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.GregorianCalendar;

import java.sql.Date;
import java.sql.Time;

/** Carpool Class
    Represents a carpool object containing several students,
    at least one of which must be a driver for the carpool to
    be considered "valid".
    */

public class Carpool implements Serializable, Comparable
{
    /** Default constructor for a Carpool.
        Sets some default values.
        */
    private String defaultStartDate = "2011-01-01";
    private String defaultTime = "07:00:00";

    public Carpool()
    {
        toSJSU = true;
        oneTime = false;
        daysOfWeek = new boolean[DAYS_IN_WEEK];
        for (int i = 0; i < DAYS_IN_WEEK; i++)
            daysOfWeek[i] = false;
        startDate = Date.valueOf(defaultStartDate);
        endDate = Date.valueOf(defaultStartDate);
        campusTime = Time.valueOf(defaultTime);
        carpoolers = new ArrayList<User>();
        location = new Location();
    }

    /** Full constructor.
        @param location the off-campus location
        @param toSJSU whether the carpool heads to or from campus
        @param startTime the departure time of the carpool
        @param endTime the arrival time of the carpool
        @param carpoolers list of students in the carpool
        */

    public Carpool(boolean toSJSU, boolean oneTime, boolean[] daysOfWeek,
                    java.sql.Date startDate, java.sql.Date endDate,
                    java.sql.Time campusTime, ArrayList<User> carpoolers,
                    Location location)
    {
        this.location = location;
        this.toSJSU = toSJSU;
        this.startDate = startDate;
        this.endDate = endDate;
        this.campusTime = campusTime;
        if (carpoolers != null)
            this.carpoolers = carpoolers;
        else this.carpoolers = new ArrayList<User>();
        this.oneTime = oneTime;
        this.daysOfWeek = new boolean[DAYS_IN_WEEK];
        for (int i = 0; i < DAYS_IN_WEEK; i++)
        {
            if (daysOfWeek.length > i)
                this.daysOfWeek[i] = daysOfWeek[i];
            else this.daysOfWeek[i] = false;
        }
    }

    // ------[ACCESSORS]------ //

    /** Returns the list of carpoolers in this carpool.
        @return the list of carpoolers in this carpool
        */

    public ArrayList<User> getCarpoolers() { return carpoolers; }

    /** Returns the carpool's days of the week in a boolean array (SMTWTFS)
        @return the carpool's days of the week in a boolean array (SMTWTFS)
        */

    public boolean[] getDaysOfWeek() { return daysOfWeek; }

    public Byte getCarpoolDays() { return carpoolDays; }

    /** Returns the carpool's days of the week in a user-friendly string
        @return the carpool's days of the week in a user-friendly string (SMTWTFS)
        */

    public String getDaysOfWeek_String()
    {
        char[] str = new char[DAYS_IN_WEEK];
        char[] days = {'S', 'M', 'T', 'W', 'T', 'F', 'S'};

        for (int i = 0; i < DAYS_IN_WEEK; i++)
            if (daysOfWeek[i])
                str[i] = days[i];
            else str[i] = '-';

        return new String(str);
    }

    /** Returns the carpool's frequency flag Byte that holds the information
     *  for the frequency of the carpool (i.e., oneTime, days of week).
     * @return the carpool's frequency flags
     */
    public Byte getFrequencyFlags_Byte()
    {
        Byte flagByte = 0;

        if(this.oneTime)
            flagByte = Byte.MIN_VALUE;

        for (int i = 0; i < DAYS_IN_WEEK; i++)
            if (daysOfWeek[i])
                flagByte = (byte) (flagByte | ( 0x1 << i ));
//                flagByte = (byte) (flagByte | (int) Math.pow(2.0, (i * 1.0)));
        
        return ((Byte) flagByte);
    }
    /** Returns the date that the carpool is scheduled to end.
        @return the endDate of the carpool
        */

    public Date getEndDate() { return endDate; }

    /** Returns the off-campus end of the carpool.
        @return the off-campus end of the carpool
        */

    public Location getLocation() { return location; }

    /** Returns whether this carpool is one-time or weekly.
        @return true if carpool is one-time, false if weekly
        */

    public boolean getOneTime() { return oneTime; }

    /** Returns the date that the carpool is scheduled to begin.
        @return the startDate of the carpool
        */

    public Date getStartDate() { return startDate; }

    /** Returns the time of day that the carpool is scheduled to either
     *  arrive at or depart from SJSU
     */

    public Time getCampusTime() { return campusTime; }

    /** Returns whether the carpool is toward or from SJSU.
        @return true if the carpool is toward SJSU, false otherwise
        */

    public boolean getToSJSU() { return toSJSU; }

    // ------[MUTATORS]------ //

    /** Adds a carpooler.
       @param buddy the carpooler to add
       @return true if successful
       */

    public boolean addCarpooler(User buddy)
    {
        if (buddy == null)
            return false;
        carpoolers.add(buddy);
        return true;
    }

    /** Sets the list of carpoolers in this carpool.
        @param carpoolers the carpoolers involved in this carpool
        @return true if set operation was successful
        */

    public boolean setCarpoolers(ArrayList<User> carpoolers)
    {
        if (carpoolers == null)
            return false;
        this.carpoolers.clear();
        this.carpoolers.addAll(carpoolers);
        return true;
    }

    /** Sets the carpool's days of the week in a boolean array (SMTWTFS)
        @param daysOfWeek the carpool's days of the week in a boolean array
        @return true if set operation was successful
        */

    public boolean setDaysOfWeek(boolean[] daysOfWeek)
    {
        if (daysOfWeek.length != DAYS_IN_WEEK)
            return false;
        for (int i = 0; i < DAYS_IN_WEEK; i++)
            this.daysOfWeek[i] = daysOfWeek[i];
        return true;
    }

    /** Sets the arrival time of the carpool.
        @param endTime the new arrival time of the carpool
        @return true if set operation was successful
        */

    public boolean setEndDate(java.sql.Date endDate)
    {
        if (endDate == null)
            return false;
        this.endDate = endDate;
        return true;
    }
    public boolean setCampusTime(java.sql.Time time)
    {
        if (time == null)
            return false;
        this.campusTime = time;
        return true;
    }
    /** Sets the off-campus end of the carpool.
        @param location the off-campus end of the carpool
        @return true if set operation was successful
        */

    public boolean setLocation(Location location)
    {
        if (location == null)
            return false;
        this.location = location;
        return true;
    }

    /** Sets whether this carpool is one-time or weekly.
        @param oneTime a flag indicating this carpool is one-time if true
        @return true if set operation was successful
        */

    public boolean setOneTime(boolean oneTime)
    {
        this.oneTime = oneTime;
        return true;
    }

    /** Sets the departure time of the carpool.
        @param startTime the new departure time of the carpool
        @return true if set operation was successful
        */

    public boolean setStartDate(Date startDate)
    {
        if (startDate == null)
            return false;
        this.startDate = startDate;
        return true;
    }

    /** Sets whether the carpool is toward or from SJSU.
        @param toSJSU whether the carpool is headed toward or away from SJSU
        @return true if set operation was successful
        */

    public boolean setToSJSU(boolean toSJSU)
    {
        this.toSJSU = toSJSU;
        return true;
    }

    // ------[MEMBER FUNCTIONS]------ //

    /** Returns whether the carpool has at least one driver.
        @return true if the carpool has at least one driver
        */

    public boolean hasAtLeastOneDriver()
    {
        for (User savvy_student : carpoolers)
            if (savvy_student.getUserType() != UserType.Passenger)
                return true;
        return false;
    }

    /** Returns whether the carpool is valid (has at least one driver).
        @return true if the carpool has at least one driver
        */

    public boolean isValid()
    {
        return hasAtLeastOneDriver();
    }

    // ------[MEMBER VARIABLES]------ //

    /** number of days in a week */
    public static final int DAYS_IN_WEEK = 7;

    /** off-campus end of the carpool route. */
    private Location location;
    /** a flag indicating whether the carpool is headed to/from SJSU */
    private boolean toSJSU;
    /** For a recurring carpool event, the beginning date of the carpool */
    private java.sql.Date startDate;
    /** For a recurring carpool event, the ending date of the carpool */
    private java.sql.Date endDate;
    /** the time of day that a carpool is scheduled to either arrive at or
        depart from campus, determined by the boolean flag toSJSU */
    private java.sql.Time campusTime;
    /** list of students involved the carpool. */
    private ArrayList<User> carpoolers;
    /** a flag indicating whether this carpool is one-time or weekly. */
    private boolean oneTime;
    /** a series of flags indicating on which days of the week
        this carpool occurs, from Sunday to Saturday (SMTWTFS). */
    private boolean[] daysOfWeek;
    private Byte carpoolDays;

    public int compareTo(Object anotherCarpool) throws ClassCastException{
        if(!(anotherCarpool instanceof Carpool)) {
            throw new ClassCastException("Only carpools can be compared.");
        }
        int ranking = 0;
        Carpool carpool = (Carpool)anotherCarpool;
        if(!(this.oneTime == carpool.oneTime) ||
                !(this.toSJSU == carpool.toSJSU) ||
                !(this.campusTime.before(carpool.campusTime))) {
            ranking = -1;
        }
         else {
//            byte temp = (byte)((this.rideDays) & (carpool.rideDays));
            for(int i = 0; i <= DAYS_IN_WEEK; i++) {
                if(this.daysOfWeek[i] == carpool.daysOfWeek[i])
                    ranking++;
            }
        }
        return ranking;
    }
}
