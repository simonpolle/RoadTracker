package be.ehb.roadtracker.data;

import java.util.List;

import be.ehb.roadtracker.domain.Location;
import be.ehb.roadtracker.domain.Locations;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by SSRBD46 on 1/03/2017.
 */

public interface LocationService
{
    String base = "api/location";

    @POST(base + "/create")
    Observable<Locations> save(@Body Locations locations);
}
