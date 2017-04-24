package be.ehb.roadtracker.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import be.ehb.roadtracker.R;
import be.ehb.roadtracker.services.implementations.CustomLocationTracker;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

public class HomeActivity extends AppCompatActivity implements CustomLocationTracker.LocationChangedListener
{
    private CustomLocationTracker customLocationTracker;

    @BindView(R.id.home_totalKmTitle)
    TextView totalKmTitle;

    @BindView(R.id.home_totalKm)
    TextView totalKm;

    @BindView(R.id.home_trajectKostTitle)
    TextView trajectKostTitle;

    @BindView(R.id.home_trajectKost)
    TextView trajectKost;

    @BindView(R.id.home_prijsKmTitle)
    TextView prijsKmTitle;

    @BindView(R.id.home_prijsKm)
    TextView prijsKm;

    @BindView(R.id.home_statusTitle)
    TextView statusTitle;

    @BindView(R.id.home_status)
    TextView status;

    @BindView(R.id.home_locationTitle)
    TextView locationTitle;

    @BindView(R.id.home_location)
    TextView location;

    @BindView(R.id.home_button)
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener()
                {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        start();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response)
                    {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
                    {/* ... */}
                }).check();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new PermissionListener()
                {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        start();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response)
                    {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
                    {/* ... */}
                }).check();
        this.customLocationTracker = new CustomLocationTracker(this);
        setupViews();
    }

    private void start()
    {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationTracker tracker = new LocationTracker(
                this,
                new TrackerSettings()
                        .setUseGPS(true)
                        .setUseNetwork(true)
                        .setUsePassive(true)
                        .setTimeBetweenUpdates(1000)
        )
        {

            @Override
            public void onLocationFound(Location location)
            {
                Toast.makeText(HomeActivity.this, location.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTimeout()
            {
                Toast.makeText(HomeActivity.this, "timeout", Toast.LENGTH_LONG).show();
            }
        };
        tracker.startListening();
    }

    private void setupViews()
    {
        this.totalKmTitle.setVisibility(View.GONE);
        this.totalKm.setVisibility(View.GONE);
        this.trajectKostTitle.setVisibility(View.GONE);
        this.trajectKost.setVisibility(View.GONE);
        this.prijsKmTitle.setVisibility(View.GONE);
        this.prijsKm.setVisibility(View.GONE);
        this.location.setText("UNKNOWN");
        this.button.setText("Waiting for GPS");
        this.button.setEnabled(false);
    }

    private void setupResultViews()
    {
        this.statusTitle.setVisibility(View.GONE);
        this.status.setVisibility(View.GONE);
        this.locationTitle.setVisibility(View.GONE);
        this.location.setVisibility(View.GONE);

        this.totalKmTitle.setVisibility(View.VISIBLE);
        this.totalKm.setVisibility(View.VISIBLE);
        this.trajectKostTitle.setVisibility(View.VISIBLE);
        this.trajectKost.setVisibility(View.VISIBLE);
        this.prijsKmTitle.setVisibility(View.VISIBLE);
        this.prijsKm.setVisibility(View.VISIBLE);
        this.button.setText("Submit route");
    }

    @Override
    public void onChange(Location location)
    {
        this.button.setText(location.toString());
    }
}
