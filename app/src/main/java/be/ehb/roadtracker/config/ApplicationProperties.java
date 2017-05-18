package be.ehb.roadtracker.config;


/**
 * Created by Simon Pollé on 26/02/2017.
 */

public class ApplicationProperties
{

    private static ApplicationProperties instance = null;
    private static String CLIENT_ID = "1";
    private static String CLIENT_SECRET = "JWRZWkEH2UBOWSbQ26d9AoqvYybV6eDg6rDvpOXO";
    private static String HOST = "http://10.16.26.190:8000";

    private ApplicationProperties()
    {
    }

    public static ApplicationProperties getInstance()
    {
        if (instance == null)
        {
            instance = new ApplicationProperties();
        }
        return instance;
    }

    public static String getClientId()
    {
        return CLIENT_ID;
    }

    public static String getClientSecret()
    {
        return CLIENT_SECRET;
    }

    public static String getHOST() { return HOST; }

    public static void setHOST(String HOST) { ApplicationProperties.HOST = HOST; }
}