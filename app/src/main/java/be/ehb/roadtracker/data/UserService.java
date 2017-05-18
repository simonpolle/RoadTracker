package be.ehb.roadtracker.data;


import be.ehb.roadtracker.domain.Route;
import be.ehb.roadtracker.domain.User;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by SSRBD46 on 1/03/2017.
 */

public interface UserService
{
    String base = "api/v1";

    @GET(base + "/user")
    Observable<User> auth();
}
