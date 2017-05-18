package be.ehb.roadtracker.ui.views;

/**
 * Created by SPLBD38 on 17/05/2017.
 */

public interface HomeView
{
    void initializeView();
    void start();
    void stop();
    void submit();
    void requestPermissions();
    void searchLocation();
    void resultView();
}
