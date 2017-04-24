package be.ehb.roadtracker.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import java.io.IOException;
import be.ehb.roadtracker.data.OAuthService;
import be.ehb.roadtracker.domain.AccessTokenResponse;
import be.ehb.roadtracker.ui.helpers.ContextHelper;
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

    public static Retrofit getClient()
    {
        if (retrofit == null)
        {
            OkHttpClient okHttpClient = new OkHttpClient();//.Builder().addInterceptor(new Interceptor()
//            {
//
//                @Override
//                public Response intercept(Chain chain) throws IOException
//                {
//                    Request request = chain.request();
//                    Request.Builder builder = request.newBuilder();
//
//                    sharedPref = PreferenceManager.getDefaultSharedPreferences(ContextHelper.getContext());
//                    String valueAccessToken = sharedPref.getString("accessToken", "accessToken");
//
//                    if (sharedPref.contains("accessToken") && !valueAccessToken.equals("accessToken")
//                            && chain.request().headers().size() == 0 )
//                    {
//                        builder.header("Authorization", "Bearer " + valueAccessToken);
//                        builder.header("Content-Type", "application/json");
//                    } else
//                    {
//                        builder.header("Content-Type", "application/x-www-form-urlencoded");
//                        builder.header("Authorization", ApiClient.authHeader());
//                    }
//                    request = builder.build();
//                    Response response = chain.proceed(request);
//
//                    if (response.code() == 401)
//                    {
//                        refreshToken(ContextHelper.getContext());
//                        valueAccessToken = sharedPref.getString("accessToken", "accessToken");
//                        builder.header("Authorization", "Bearer " + valueAccessToken);
//                        request = builder.build();
//                        return chain.proceed(request);
//                    }
//
//                    return response;
//                }
//            }).build();

            retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(ApplicationProperties.getHOST())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }


    private static void refreshToken(final Context context)
    {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(ContextHelper.getContext());
        final String valueRefreshToken = sharedPref.getString("refreshToken", "refreshToken");

        service = ApiClient.getClient().create(OAuthService.class);
        Log.d("ApiClient", "RefreshToken: " + valueRefreshToken);

        retrofit2.Response<AccessTokenResponse> response = null;
        try
        {
            response = service
                    .getRefreshToken("refresh_token", ApplicationProperties.getClientId(), ApplicationProperties.getClientSecret(), valueRefreshToken).execute();
            if (response.body() != null && response.isSuccessful())
            {
                AccessTokenResponse accessResponse = response.body();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("accessToken",
                        accessResponse.getAccess_token());
                editor.putString("refreshToken",
                        accessResponse.getRefresh_token());
                editor.commit();
            }
            else
            {
                Log.d("ApiClient", "BAD REFRESH response: " + response.raw().body());
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.realdolmen.ticker.ACTION_LOGOUT");
                ContextHelper.getContext().sendBroadcast(broadcastIntent);
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

    private static String authHeader()
    {
        String clientId = ApplicationProperties.getClientId();
        String clientSecret = ApplicationProperties.getClientSecret();
        String merge = clientId + ":" + clientSecret;

        byte[] credentials = merge.getBytes();
        String basicAuth = "Basic " + Base64.encodeToString(credentials, Base64.DEFAULT);
        return basicAuth.trim();
    }
}
