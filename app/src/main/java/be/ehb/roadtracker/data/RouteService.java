package be.ehb.roadtracker.data;


import java.util.List;

import be.ehb.roadtracker.domain.Route;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by SSRBD46 on 1/03/2017.
 */

public interface RouteService
{
    String base = "api/route";

    @GET(base)
    Observable<List<Route>> findAll(@Query("page") int page);

    @GET(base + "/{id}")
    Observable<Route> findOne(@Path("id") long id);

    @POST(base + "/create")
    Observable<Route> save(@Body Route route);

    @PUT(base + "/update")
    Observable<Route> update(@Body Route route);

    @DELETE(base + "/delete/{id}")
    Observable<Route> delete(@Path("id") Long id);
}
