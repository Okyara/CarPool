package business;

import java.io.Serializable;

/** Location Class
    Represents an address or intersection.
    */

public class Location implements Serializable
{
    // ------[CONSTRUCTORS]------ //

    /** Default constructor. */

    public Location() { ; }

    /** Full constructor.
        @param street the street address/intersection
        @param city the city name
        @param zip the zip code
        */

    public Location(String street, String city, int zip)
    {
        if (!setStreet(street)) street = "";
        if (!setCity(city)) city = "";
        if (!setZip(zip)) zip = 0;
    }

    // ------[ACCESSORS]------ //

    /** Returns the city
        @return the city
        */

    public String getCity() { return city; }

    /** Returns the street address/intersection
        @return the street address/intersection
        */

    public String getStreet() { return street; }

    /** Returns the zip code
        @return the zip code
        */

    public int getZip() { return zip; }

    // ------[MUTATORS]------ //

    /** Sets the city name.
        @param city the new name of the city
        @return true if successful
        */

    public boolean setCity(String city)
    {
        if (city == null)
            return false;
        this.city = city;
        return true;
    }

    /** Sets value of the street address / intersection.
        @param string the new street address / intersection
        @return true if successful
        */

    public boolean setStreet(String street)
    {
        if (street == null)
            return false;
        this.street = street;
        return true;
    }

    /** Sets the value of the zip code.
        @param zip the new zip code
        @return true if successful
        */

    public boolean setZip(int zip)
    {
        if (zip >= 1000000000 || zip < 0) // zip should be 5 or 9 digits
            return false;
        this.zip = zip;
        return true;
    }

    // ------[MEMBER VARIABLES]------ //

    /** street address or intersection */
    private String street;
    /** city name */
    private String city;
    /** zip code */
    private int zip;
}
