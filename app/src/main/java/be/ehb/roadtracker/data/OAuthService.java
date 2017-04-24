package be.ehb.roadtracker.data;


import be.ehb.roadtracker.domain.AccessTokenResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Simon Pollé on 26/03/2017.
 */

public interface OAuthService
{

    @FormUrlEncoded
    @POST("oauth/token")
    Observable<AccessTokenResponse> getAccessToken(@Field("grant_type") String grantType,
                                                   @Field("client_id") String client_id,
                                                   @Field("client_secret") String client_secret,
                                                   @Field("username") String username,
                                                   @Field("password") String password);

    @FormUrlEncoded
    @POST("oauth/token")
    Call<AccessTokenResponse> getRefreshToken(@Field("grant_type") String grantType,
                                                   @Field("client_id") String client_id,
                                                   @Field("client_secret") String client_secret,
                                                   @Field("refresh_token") String refresh_token);

}