package be.ehb.roadtracker.ui.activities;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import be.ehb.roadtracker.R;
import be.ehb.roadtracker.services.implementations.CustomLocationTracker;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
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
        this.customLocationTracker = new CustomLocationTracker(this);
        enableGpsPermission();
        setupViews();
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void enableGpsPermission()
    {
        enableGpsPermission2();
    }

    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    void enableGpsPermission2()
    {
        SmartLocation.with(this).location()
                .start(new OnLocationUpdatedListener()
                {
                    @Override
                    public void onLocationUpdated(Location location)
                    {
                       Toast.makeText(HomeActivity.this, "test", Toast.LENGTH_LONG).show();
                    }
                });
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
