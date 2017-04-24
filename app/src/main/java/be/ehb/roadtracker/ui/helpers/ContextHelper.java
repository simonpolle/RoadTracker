package be.ehb.roadtracker.ui.helpers;

import android.app.Application;
import android.content.Context;

/**
 * Created by SSRBD46 on 6/04/2017.
 */

public class ContextHelper extends Application
{
    private static ContextHelper instance;

    public static ContextHelper getInstance()
    {
        return instance;
    }

    public static Context getContext()
    {
        return instance;
    }

    @Override
    public void onCreate()
    {
        instance = this;
        super.onCreate();
    }
}
