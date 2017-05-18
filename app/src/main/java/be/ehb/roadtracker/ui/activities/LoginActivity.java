package be.ehb.roadtracker.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import be.ehb.roadtracker.R;
import be.ehb.roadtracker.domain.AccessTokenResponse;
import be.ehb.roadtracker.presenters.LoginPresenterImpl;
import be.ehb.roadtracker.ui.views.LoginView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import java.util.List;

/**
 * Created by SSRBD46 on 22/02/2017.
 */

public class LoginActivity extends AppCompatActivity implements ValidationListener, LoginPresenterImpl.LoginPresenterListener, LoginView
{

    @Email
    @NotEmpty
    @BindView(R.id.login_email)
    EditText email;

    @Password
    @NotEmpty
    @BindView(R.id.login_password)
    EditText password;

    @BindView(R.id.login_loginBtn)
    Button loginBtn;

    @BindView(R.id.login_progressBar)
    ProgressBar progressBar;

    private String emailString;
    private Validator validator;
    private LoginPresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initializeView();
    }

    @Override
    public void initializeView()
    {
        validator = new Validator(this);
        validator.setValidationListener(this);
        loginBtn.getBackground().setAlpha(150);

        this.loginBtn.setEnabled(false);
        this.progressBar.setVisibility(View.INVISIBLE);

        email.setText("admin@admin.be");
        password.setText("secret");
        presenter = new LoginPresenterImpl(getApplicationContext(), this);
    }

    @OnClick(R.id.login_loginBtn)
    @Override
    public void login()
    {
        loginBtn.setText("Authenticating...");
        loginBtn.getBackground().setAlpha(150);
        this.emailString = this.email.getText().toString();
        this.progressBar.setVisibility(View.VISIBLE);

        String email = this.emailString;
        String password = this.password.getText().toString();
        presenter.login(email, password);
    }

    @OnTextChanged(value = {R.id.login_password, R.id.login_email})
    @Override
    public void validate()
    {
        this.validator.validate();
    }

    @Override
    public void showErrors()
    {
        loginBtn.setText("Authentication failed");
        loginBtn.getBackground().setAlpha(255);
        this.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void navigateToHome()
    {
        finish();
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    public void onValidationSucceeded()
    {
        this.loginBtn.setEnabled(true);
        loginBtn.getBackground().setAlpha(255);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors)
    {
        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText && !((EditText) view).getText().toString().isEmpty())
            {
                ((EditText) view).setError(message);
            }
        }

        this.loginBtn.setEnabled(false);
        loginBtn.getBackground().setAlpha(150);
    }

    @Override
    public void authenticated(AccessTokenResponse response)
    {
        navigateToHome();
    }

    @Override
    public void notAuthenticated()
    {
        showErrors();
    }
}

