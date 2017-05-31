package be.ehb.roadtracker.config;


/**
 * Created by Simon Pollé on 26/02/2017.
 */

public class ApplicationProperties
{

    private static ApplicationProperties instance = null;
    private static String CLIENT_ID = "1";
    private static String CLIENT_SECRET = "0Poa21hDEL6HRZe6G9oMreZWcUao2ZmdOx4ZD6QV";
    private static String ACCESS_TOKEN = "";
    private static String REFRESH_TOKEN = "";
    private static String HOST = "http://192.168.0.163:8000";

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

    public static String getAccessToken() { return ACCESS_TOKEN; }
    public static String getRefreshToken() { return REFRESH_TOKEN; }

    public static void setHOST(String HOST) { ApplicationProperties.HOST = HOST; }
    public static void setAccessToken(String ACCESS_TOKEN) { ApplicationProperties.ACCESS_TOKEN = ACCESS_TOKEN; }
    public static void setRefreshToken(String REFRESH_TOKEN) { ApplicationProperties.REFRESH_TOKEN = REFRESH_TOKEN; }
}