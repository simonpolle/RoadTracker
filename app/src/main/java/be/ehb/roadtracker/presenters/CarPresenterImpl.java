package be.ehb.roadtracker.presenters;

import android.content.Context;
import android.widget.Toast;
import be.ehb.roadtracker.config.ApiClient;
import be.ehb.roadtracker.data.CarService;
import be.ehb.roadtracker.data.UserService;
import be.ehb.roadtracker.domain.Car;
import be.ehb.roadtracker.domain.User;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Simon Pollé on 15/03/2017.
 */

public class CarPresenterImpl implements CarPresenter
{

    private final Context context;
    private CarPresenterListener carPresenterListener = null;
    private CarService service;

    public interface CarPresenterListener
    {
        void successfull(Car response);
        void unsuccessfull();
    }

    public CarPresenterImpl(Context context,
        CarPresenterImpl.CarPresenterListener listener)
    {
        this.context = context;
        this.carPresenterListener = listener;
    }

    public void findOne(long id)
    {
        service = ApiClient.getClient().create(CarService.class);

        service.findOne(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Car>()
            {
                @Override
                public void onCompleted()
                {
                }

                @Override
                public void onError(Throwable e)
                {
                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    carPresenterListener.unsuccessfull();
                }

                @Override
                public void onNext(Car response)
                {
                    if (response != null)
                    {
                        carPresenterListener.successfull(response);
                    } else
                    {
                        carPresenterListener.unsuccessfull();
                    }
                }
            });
    }
}
