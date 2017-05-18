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
    private final RoutePresenterListener listener;
    private RouteService service;

    public interface RoutePresenterListener
    {
        void successfull(List<Route> response);
        void unsuccessfull();
    }

    public RoutePresenterImpl(Context context, RoutePresenterImpl.RoutePresenterListener listener)
    {
        this.context = context;
        this.listener = listener;
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
                        listener.unsuccessfull();
                    }

                    @Override
                    public void onNext(List<Route> response)
                    {
                        if (response != null)
                            listener.successfull(response);
                        else
                            listener.unsuccessfull();
                    }
                });
    }
}
