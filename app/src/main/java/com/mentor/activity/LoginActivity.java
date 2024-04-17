package com.mentor.activity;

import static com.mentor.utils.Utils.BASE_URL;
import static com.mentor.utils.Utils.CURRENT_USER_EXTRA;
import static com.mentor.utils.Utils.showToast;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mentor.R;
import com.mentor.dto.form.SignInForm;
import com.mentor.dto.view.UserView;
import com.mentor.requests.RequestUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.mentor.utils.Utils;

import java.util.Arrays;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private ProgressDialog prgDialog;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prgDialog = new ProgressDialog(this);
        emailInput = findViewById(R.id.loginEmailInput);
        passwordInput = findViewById(R.id.loginPasswordInput);

        TextView forgotPasswordText = findViewById(R.id.loginForgotPasswordText);
        forgotPasswordText.setOnClickListener(this::forgotPasswordOnClick);

        Button signInBtn = findViewById(R.id.loginSignInBtn);
        signInBtn.setOnClickListener(this::signInOnClick);

        Button createAccount = findViewById(R.id.loginCreateAccountBtn);
        createAccount.setOnClickListener(this::createAccountOnClick);
    }

    private void createAccountOnClick(View view) {
        startActivity(new Intent(this, RegisterNameActivity.class));
    }

    private void signInOnClick(View view) {
        String email = Objects.requireNonNull(emailInput.getText()).toString();
        String password = Objects.requireNonNull(passwordInput.getText()).toString();

        if(Utils.isNotNull(email) && Utils.isNotNull(password)) {
            if (Utils.validateEmail(email)) {
                invokeWS(email, password);
            }else {
                showToast(this, "Моля вкарайте валиден имейл");
            }
        }else {
            showToast(this, "Моля попълнете всички полета");
        }
    }


    private void forgotPasswordOnClick(View view) {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    private void invokeWS(String email, String password) {
        prgDialog.show();

        SignInForm form = new SignInForm(email, password);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RequestUser requestUser = retrofit.create(RequestUser.class);

        Call<UserView> call = requestUser.signInPost(form);

        call.enqueue(new Callback<UserView>() {
            @Override
            public void onResponse(Call<UserView> call, Response<UserView> response) {
                prgDialog.hide();
                if (response.code() == 401) {
                    showToast(LoginActivity.this, "Невалидни потребителски данни");
                } else if (response.code() == 404) {
                    showToast(LoginActivity.this, "Страницата не е намерена");
                } else if (response.code() == 500) {
                    showToast(LoginActivity.this, "Сървърна грешка");
                } else {
                    Intent homePageIntent = new Intent(LoginActivity.this, HomePageActivity.class);
                    Bundle bundle = new Bundle();
                    UserView userView = response.body();
                    String userViewJson = gson.toJson(userView);
                    bundle.putString(CURRENT_USER_EXTRA, userViewJson);
                    homePageIntent.putExtras(bundle);
                    startActivity(homePageIntent);
                }
            }

            @Override
            public void onFailure(Call<UserView> call, Throwable throwable) {

            }
        });
    }

}
