package be.ehb.roadtracker.presenters;

import android.content.Context;
import android.widget.Toast;
import be.ehb.roadtracker.config.ApiClient;
import be.ehb.roadtracker.data.RouteService;
import be.ehb.roadtracker.data.UserService;
import be.ehb.roadtracker.domain.Route;
import be.ehb.roadtracker.domain.User;
import java.util.List;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Simon Pollé on 15/03/2017.
 */

public class UserPresenterImpl implements UserPresenter
{

    private final Context context;
    private UserPresenterAuthenticatedListener authenticatedListener = null;
    private UserService service;

    public interface UserPresenterAuthenticatedListener
    {

        void successfull(User response);

        void unsuccessfull();
    }

    public UserPresenterImpl(Context context,
        UserPresenterImpl.UserPresenterAuthenticatedListener listener)
    {
        this.context = context;
        this.authenticatedListener = listener;
    }

    public void authenticatedUser()
    {
        service = ApiClient.getClient().create(UserService.class);

        service.authenticatedUser()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<User>()
            {
                @Override
                public void onCompleted()
                {
                }

                @Override
                public void onError(Throwable e)
                {
                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    authenticatedListener.unsuccessfull();
                }

                @Override
                public void onNext(User response)
                {
                    if (response != null)
                    {
                        authenticatedListener.successfull(response);
                    } else
                    {
                        authenticatedListener.unsuccessfull();
                    }
                }
            });
    }
}
