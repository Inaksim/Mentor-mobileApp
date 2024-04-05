package com.mentor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mentor.R;
import com.mentor.dto.form.SignInForm;
import com.mentor.dto.view.UserView;
import com.mentor.requests.RequestUser;

import org.json.JSONException;

import cz.msebera.android.httpclient.entity.StringEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    Button sign_in_btn;
    EditText email, password;
    TextView textView, sIuser;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        textView = findViewById(R.id.textView2);
        sIuser = findViewById(R.id.textView3);


        sign_in_btn = findViewById(R.id.sign_in_btn);

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String emailString = email.getText().toString();
                    String passwordString = password.getText().toString();
                    sign_in(emailString, passwordString);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.0.190:8080")
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        RequestUser requestUser = retrofit.create(RequestUser.class);
//        requestUser.test().enqueue(new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) {
//
//                textView.setText(response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable throwable) {
//                textView.setText(throwable.getMessage());
//            }
//        });
    }
    public void goHome(View view) {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void sign_in(String email, String password) throws JSONException {
        SignInForm form = new SignInForm();
        form.setEmail(email);
        form.setPassword(password);

        StringEntity entity1 = new StringEntity(form.toString(), "UTF-8");
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.190:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        sIuser.setText(form.toString());
        RequestUser requestUser = retrofit.create(RequestUser.class);

        Call<UserView> call = requestUser.signInPost(form);

        call.enqueue(new Callback<UserView>() {
            @Override
            public void onResponse(Call<UserView> call, Response<UserView> response) {
                Intent homePageIntent = new Intent(SignInActivity.this, HomePageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("current_user", response.toString());
                homePageIntent.putExtras(bundle);
                startActivity(homePageIntent);
//                sIuser.setText(response.body().getFirstName());
//                sIuser.setText(response.toString());


            }

            @Override
            public void onFailure(Call<UserView> call, Throwable throwable) {
//                sIuser.setText(throwable.hashCode());

                sIuser.setText(throwable.toString());
            }
        });


    }
}