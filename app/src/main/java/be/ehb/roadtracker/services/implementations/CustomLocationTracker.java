package be.ehb.roadtracker.services.implementations;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import be.ehb.roadtracker.services.interfaces.LocationTracker;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import rx.Subscription;
import rx.functions.Action1;

public class CustomLocationTracker implements LocationTracker
{
    private List<Location> locations;
    private final LocationChangedListener listener;

    public interface LocationChangedListener
    {
        void onChange(Location location);
    }

    public CustomLocationTracker(LocationChangedListener listener)
    {
        this.listener = listener;
        this.locations = new ArrayList<>();
    }

    @Override
    public void start(final Context context)
    {
        SmartLocation.with(context).location()
                .start(new OnLocationUpdatedListener()
                {
                    @Override
                    public void onLocationUpdated(Location location)
                    {
                        Toast.makeText(context, location.getProvider(), Toast.LENGTH_LONG).show();
                        listener.onChange(location);
                    }
                });
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void stop()
    {
        //clear locaiton list and api request to store route
    }

    @Override
    public void save(Location location)
    {
        locations.add(location);
    }
}
