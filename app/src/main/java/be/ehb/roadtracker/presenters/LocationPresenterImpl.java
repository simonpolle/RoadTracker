package be.ehb.roadtracker.presenters;

import android.content.Context;
import android.widget.Toast;

import be.ehb.roadtracker.config.ApiClient;
import be.ehb.roadtracker.data.LocationService;
import be.ehb.roadtracker.domain.Locations;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Simon Pollé on 15/03/2017.
 */

public class LocationPresenterImpl implements LocationPresenter
{

    private final Context context;
    private LocationPresenterListener locationPresenterListener = null;
    private LocationService service;

    public interface LocationPresenterListener
    {
        void successfull(Locations response);
        void unsuccessfull();
    }

    public LocationPresenterImpl(Context context,
                                 LocationPresenterImpl.LocationPresenterListener listener)
    {
        this.context = context;
        this.locationPresenterListener = listener;
    }

    public void save(Locations locations)
    {
        service = ApiClient.getClient().create(LocationService.class);

        service.save(locations)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Locations>()
            {
                @Override
                public void onCompleted()
                {
                }

                @Override
                public void onError(Throwable e)
                {
                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    locationPresenterListener.unsuccessfull();
                }

                @Override
                public void onNext(Locations response)
                {
                    if (response != null)
                    {
                        locationPresenterListener.successfull(response);
                    } else
                    {
                        locationPresenterListener.unsuccessfull();
                    }
                }
            });
    }
}
