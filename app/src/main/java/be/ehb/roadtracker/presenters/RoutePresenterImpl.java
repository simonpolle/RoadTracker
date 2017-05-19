package be.ehb.roadtracker.presenters;

import android.content.Context;
import android.widget.Toast;
import be.ehb.roadtracker.config.ApiClient;
import be.ehb.roadtracker.data.RouteService;
import be.ehb.roadtracker.domain.Route;
import java.util.List;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Simon Pollé on 15/03/2017.
 */

public class RoutePresenterImpl implements RoutePresenter
{

    private final Context context;
    private RoutePresenterFindAllListener findAllListener = null;
    private RoutePresenterGetByIdListener getByIdListener = null;
    private RouteService service;

    public interface RoutePresenterFindAllListener
    {
        void successfull(List<Route> response);
        void unsuccessfull();
    }

    public interface RoutePresenterGetByIdListener
    {
        void successfull(Route response);
        void unsuccessfull();
    }

    public RoutePresenterImpl(Context context, RoutePresenterImpl.RoutePresenterFindAllListener listener)
    {
        this.context = context;
        this.findAllListener = listener;
    }

    public RoutePresenterImpl(Context context, RoutePresenterImpl.RoutePresenterGetByIdListener listener)
    {
        this.context = context;
        this.getByIdListener = listener;
    }

    public void findAll(int page)
    {
        service = ApiClient.getClient().create(RouteService.class);

        service.findAll(page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Route>>()
                {
                    @Override
                    public void onCompleted()
                    {
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        findAllListener.unsuccessfull();
                    }

                    @Override
                    public void onNext(List<Route> response)
                    {
                        if (response != null)
                            findAllListener.successfull(response);
                        else
                            findAllListener.unsuccessfull();
                    }
                });
    }

    @Override
    public void findOne(long id)
    {
        service = ApiClient.getClient().create(RouteService.class);

        service.findOne(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Route>()
            {
                @Override
                public void onCompleted()
                {
                }

                @Override
                public void onError(Throwable e)
                {
                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    getByIdListener.unsuccessfull();
                }

                @Override
                public void onNext(Route response)
                {
                    if (response != null)
                        getByIdListener.successfull(response);
                    else
                        getByIdListener.unsuccessfull();
                }
            });
    }
}
