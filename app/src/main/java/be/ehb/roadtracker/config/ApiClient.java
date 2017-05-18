package be.ehb.roadtracker.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import be.ehb.roadtracker.config.ApplicationProperties;
import be.ehb.roadtracker.data.OAuthService;
import be.ehb.roadtracker.domain.AccessTokenResponse;
import be.ehb.roadtracker.ui.helpers.ContextHelper;
import com.google.android.gms.common.api.Api.ApiOptions;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SSRBD46 on 1/03/2017.
 */

public class ApiClient
{

    private static Retrofit retrofit = null;
    private static OAuthService service;
    private static SharedPreferences sharedPref;
    private ApplicationProperties applicationProperties;

    public static Retrofit getClient()
    {
        if (retrofit == null)
        {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor()
            {

                @Override
                public Response intercept(Chain chain) throws IOException
                {
                    Request request = chain.request();
                    Request.Builder builder = request.newBuilder();

                    String accessToken = ApplicationProperties.getAccessToken();

                    if (!accessToken.isEmpty())
                    {
                        builder.header("Authorization", "Bearer " + accessToken);
                        builder.header("Content-Type", "application/json");
                    } else
                    {
                        request = builder.build();
                        return chain.proceed(request);
                    }
                    request = builder.build();
                    Response response = chain.proceed(request);

                    if (response.code() == 401)
                    {
                        refreshToken();
                        accessToken = ApplicationProperties.getAccessToken();
                        builder.header("Authorization", "Bearer " + accessToken);
                        request = builder.build();
                        return chain.proceed(request);
                    }

                    return response;
                }
            }).build();

            retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(ApplicationProperties.getHOST())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        }
        return retrofit;
    }


    private static void refreshToken()
    {
        String refreshToken = ApplicationProperties.getRefreshToken();

        service = ApiClient.getClient().create(OAuthService.class);
        retrofit2.Response<AccessTokenResponse> response = null;
        try
        {
            response = service
                .getRefreshToken("refresh_token", ApplicationProperties.getClientId(), ApplicationProperties.getClientSecret(),
                    ApplicationProperties.getRefreshToken()).execute();
            if (response.body() != null && response.isSuccessful())
            {
                AccessTokenResponse accessTokenResponse = response.body();
                SharedPreferences.Editor editor = sharedPref.edit();
                ApplicationProperties.setAccessToken(accessTokenResponse.access_token);
                ApplicationProperties.setRefreshToken(accessTokenResponse.refresh_token);
                editor.commit();
            }
            else
            {
                Log.d("Http:", "Bad refresh token: " + response.raw().body());
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (response != null)
            {
                response.raw().body().close();
            }
        }
    }
}