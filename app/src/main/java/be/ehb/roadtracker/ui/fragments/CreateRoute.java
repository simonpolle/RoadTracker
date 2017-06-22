package be.ehb.roadtracker.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import be.ehb.roadtracker.R;
import be.ehb.roadtracker.domain.Locations;
import be.ehb.roadtracker.presenters.LocationPresenter;
import be.ehb.roadtracker.presenters.LocationPresenterImpl;
import be.ehb.roadtracker.presenters.LoginPresenterImpl;
import be.ehb.roadtracker.presenters.RoutePresenterImpl;
import be.ehb.roadtracker.ui.views.HomeView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

public class CreateRoute extends Fragment implements HomeView, LocationPresenterImpl.LocationPresenterListener, AdapterView.OnItemSelectedListener
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

    private List<Location> locations;
    private boolean isStarted;
    private Geocoder geocoder;
    private List<Address> addresses;
    private LocationPresenterImpl presenter;

    public CreateRoute()
    {
    }

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


//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, categories);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(dataAdapter);
        return view;
    }

    @Override
    public void initializeView()
    {
        this.locations = new ArrayList<>();
        this.isStarted = false;
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        this.stop.setVisibility(View.GONE);
        this.submit.setVisibility(View.GONE);
        this.location.setText("UNKNOWN");
        this.searchAnimation.setVisibility(View.VISIBLE);
        this.status.setText("Waiting for GPS");
        status.setTextColor(Color.RED);
        this.start.setEnabled(false);
        presenter = new LocationPresenterImpl(getContext(), this);
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
                    public void onPermissionDenied(PermissionDeniedResponse response)
                    {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                                   PermissionToken token)
                    {
                    }
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
                    public void onPermissionDenied(PermissionDeniedResponse response)
                    {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                                   PermissionToken token)
                    {
                    }
                }).check();
    }

    @Override
    public void searchLocation()
    {
        if (ActivityCompat
                .checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
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
                Toast.makeText(getContext(), l.toString(), Toast.LENGTH_SHORT).show();
                if (!isStarted)
                {
                    locations.add(l);
                    status.setText("Ready");
                    status.setTextColor(Color.YELLOW);
                    try
                    {
                        addresses = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
                        if (!addresses.isEmpty())
                            location.setText(addresses.get(0).getAddressLine(0));
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    searchAnimation.hide();
                    start.setEnabled(true);
                } else
                {
                    locations.add(l);
                    try
                    {
                        addresses = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
                        if (!addresses.isEmpty())
                            location.setText(addresses.get(0).getAddressLine(0));
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onTimeout()
            {
                status.setText("Could not find location");
            }
        };
        tracker.startListening();
    }

    @OnClick(R.id.home_start)
    public void start()
    {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure you want to start?")
                .setContentText("You won't be able to pause this route!")
                .setConfirmText("Yes,I understand!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                {
                    @Override
                    public void onClick(SweetAlertDialog sDialog)
                    {
                        sDialog
                                .setTitleText("Started!")
                                .setContentText("You can drive to your desired location!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        isStarted = true;
                        start.setVisibility(View.GONE);
                        stop.setVisibility(View.VISIBLE);
                        searchAnimation.setVisibility(View.VISIBLE);
                        status.setText("Running");
                        status.setTextColor(Color.GREEN);
                    }
                })
                .show();
    }

    @OnClick(R.id.home_stop)
    public void stop()
    {
        stop.setVisibility(View.GONE);
        submit.setVisibility(View.VISIBLE);
        searchAnimation.hide();
        status.setText("Stopped");
        status.setTextColor(Color.YELLOW);
    }

    @OnClick(R.id.home_submit)
    public void submit()
    {
        List<be.ehb.roadtracker.domain.Location> convertedLocations = new ArrayList<>();

        for (Location location : locations)
        {
            convertedLocations.add(new be.ehb.roadtracker.domain.Location(location.getLatitude(), location.getLongitude(), 1));
        }

        presenter.save(new Locations(convertedLocations));
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

    @Override
    public void successfull(Locations response)
    {
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText("Route successfully added!")
                .show();

        searchLocation();
        initializeView();
        this.locations.clear();

    }

    @Override
    public void unsuccessfull()
    {
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText("Route successfully added!")
                .show();

        searchLocation();
        initializeView();
        this.locations.clear();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}
