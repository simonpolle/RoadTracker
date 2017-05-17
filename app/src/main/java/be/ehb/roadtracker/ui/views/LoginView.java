package be.ehb.roadtracker.ui.views;

/**
 * Created by Simon Pollé on 17/05/2017.
 */

public interface LoginView
{
    void initializeView();
    void login();
    void validate();
    void showErrors();
    void navigateToHome();
}
