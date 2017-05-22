package be.ehb.roadtracker.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import be.ehb.roadtracker.R;
import be.ehb.roadtracker.domain.Car;
import be.ehb.roadtracker.domain.Route;
import be.ehb.roadtracker.domain.User;
import be.ehb.roadtracker.presenters.CarPresenterImpl;
import be.ehb.roadtracker.presenters.RoutePresenterImpl;
import be.ehb.roadtracker.presenters.UserPresenterImpl;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RouteDetailActivity extends AppCompatActivity implements RoutePresenterImpl.RoutePresenterGetByIdListener,
    UserPresenterImpl.UserPresenterAuthenticatedListener, CarPresenterImpl.CarPresenterListener
{

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.route_detail_nameDriver)
    TextView driver;

    @BindView(R.id.route_detail_distanceTravelled)
    TextView distanceTravelled;

    @BindView(R.id.route_detail_licencePlate)
    TextView licencePlate;

    @BindView(R.id.route_detail_totalCost)
    TextView totalCost;

    private RoutePresenterImpl routePresenter;
    private UserPresenterImpl userPresenter;
    private CarPresenterImpl carPresenter;
    private Route route;
    private boolean isFetching = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        ButterKnife.bind(this);
        initializeView();
    }

    private void initializeView()
    {
        Intent intent = getIntent();
        long id = intent.getExtras().getLong("id");
        driver.setVisibility(View.GONE);
        distanceTravelled.setVisibility(View.GONE);
        licencePlate.setVisibility(View.GONE);
        totalCost.setVisibility(View.GONE);
        routePresenter = new RoutePresenterImpl(this, this);
        userPresenter = new UserPresenterImpl(this, this);
        carPresenter = new CarPresenterImpl(this, this);
        routePresenter.findOne(id);
        userPresenter.authenticatedUser();
    }

    @Override
    public void successfull(Route response)
    {
        distanceTravelled.setText(String.valueOf(response.getDistance_travelled()));
        totalCost.setText(String.valueOf(response.getTotal_cost()));
        carPresenter.findOne(response.getCar_id());
    }

    @Override
    public void successfull(User response)
    {
        driver.setText(response.getFirst_name() + " " + response.getLast_name());
    }

    @Override
    public void successfull(Car response)
    {
        licencePlate.setText(response.getLicence_plate());
        progressBar.setVisibility(View.GONE);
        driver.setVisibility(View.VISIBLE);
        distanceTravelled.setVisibility(View.VISIBLE);
        licencePlate.setVisibility(View.VISIBLE);
        totalCost.setVisibility(View.VISIBLE);
    }

    @Override
    public void unsuccessfull()
    {
        driver.setText("Not found");
        distanceTravelled.setText("Not found");
        licencePlate.setText("Not found");
        totalCost.setText("Not found");
    }
}
