package be.ehb.roadtracker.ui.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import be.ehb.roadtracker.R;
import be.ehb.roadtracker.ui.views.HomeView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wang.avi.AVLoadingIndicatorView;
import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import java.text.DecimalFormat;

public class CreateRoute extends Fragment implements HomeView
{
    @BindView(R.id.home_statusTitle)
    TextView statusTitle;

    @BindView(R.id.home_status)
    TextView status;

    @BindView(R.id.home_locationTitle)
    TextView locationTitle;

    @BindView(R.id.home_location)
    TextView location;

    @BindView(R.id.home_searchAnimation)
    AVLoadingIndicatorView searchAnimation;

    @BindView(R.id.home_start)
    Button start;

    @BindView(R.id.home_stop)
    Button stop;

    @BindView(R.id.home_submit)
    Button submit;

    public CreateRoute() {}

    public static CreateRoute newInstance(int position)
    {
        Bundle args = new Bundle();
        CreateRoute fragment = new CreateRoute();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_route, container, false);
        ButterKnife.bind(this, view);
        initializeView();
        requestPermissions();
        return view;
    }

    @OnClick(R.id.home_start)
    public void start()
    {
        start.setVisibility(View.GONE);
        stop.setVisibility(View.VISIBLE);
        searchAnimation.setVisibility(View.VISIBLE);
        status.setText("Running");
    }

    @OnClick(R.id.home_stop)
    public void stop()
    {
        stop.setVisibility(View.GONE);
        submit.setVisibility(View.VISIBLE);
        searchAnimation.hide();
    }

    @OnClick(R.id.home_submit)
    public void submit()
    {

    }

    @Override
    public void initializeView()
    {
        this.stop.setVisibility(View.GONE);
        this.submit.setVisibility(View.GONE);
        this.location.setText("UNKNOWN");
        this.searchAnimation.setVisibility(View.VISIBLE);
        this.status.setText("Waiting for GPS");
        this.start.setEnabled(false);
    }

    @Override
    public void requestPermissions()
    {
        Dexter.withActivity(getActivity())
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(new PermissionListener()
            {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse response)
                {
                    searchLocation();
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse response) {}

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {}
            }).check();

        Dexter.withActivity(getActivity())
            .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(new PermissionListener()
            {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse response)
                {
                    searchLocation();
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse response) {}

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {}
            }).check();
    }

    @Override
    public void searchLocation()
    {
        if (ActivityCompat
            .checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationTracker tracker = new LocationTracker(getContext(),
            new TrackerSettings()
                .setUseGPS(true)
                .setUseNetwork(false)
                .setUsePassive(false)
                .setTimeBetweenUpdates(1000)
        )
        {
            @Override
            public void onLocationFound(Location l)
            {
                status.setText("Ready");
                location.setText(String.valueOf(new DecimalFormat("##.####").format(l.getLatitude())) + "\n" + String.valueOf(new DecimalFormat("##.####").format(l.getLongitude())));
                searchAnimation.hide();
                start.setEnabled(true);
            }

            @Override
            public void onTimeout()
            {
                status.setText("Could not find location");
            }
        };
        tracker.startListening();
    }

    @Override
    public void resultView()
    {
        this.statusTitle.setVisibility(View.GONE);
        this.status.setVisibility(View.GONE);
        this.locationTitle.setVisibility(View.GONE);
        this.location.setVisibility(View.GONE);
        this.start.setVisibility(View.GONE);
        this.stop.setVisibility(View.VISIBLE);
    }

}
