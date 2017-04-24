package be.ehb.roadtracker.services.interfaces;


import android.content.Context;
import android.location.Location;


public interface LocationTracker
{
    void start(Context context);
    void pause();
    void stop();
    void save(Location location);
}
