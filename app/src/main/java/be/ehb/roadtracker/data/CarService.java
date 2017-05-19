package be.ehb.roadtracker.data;


import be.ehb.roadtracker.domain.Car;
import be.ehb.roadtracker.domain.Route;
import be.ehb.roadtracker.domain.User;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Simon Pollé on 1/03/2017.
 */

public interface CarService
{
    String base = "api/car";

    @GET(base + "/{id}")
    Observable<Car> findOne(@Path("id") long id);
}
