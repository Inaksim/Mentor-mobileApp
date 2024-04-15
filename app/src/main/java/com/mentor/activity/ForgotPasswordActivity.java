package com.mentor.activity;

import static com.mentor.utils.Utils.EMAIL_EXTRA;
import static com.mentor.utils.Utils.PARENT_EXTRA;
import static com.mentor.utils.Utils.isNotNull;
import static com.mentor.utils.Utils.showToast;
import static com.mentor.utils.Utils.validateEmail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mentor.R;
import com.mentor.dto.form.ResetPasswordForm;
import com.mentor.dto.view.UserView;
import com.mentor.requests.RequestUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ImageView backImage;
    private TextInputEditText emailInput;
    private Button submitBtn;
    private ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        prgDialog = new ProgressDialog(this);

        backImage = findViewById(R.id.forgotPasswordBackImage);
        backImage.setOnClickListener(this::backImageOnClick);

        emailInput = findViewById(R.id.forgotPasswordEmailInput);

        submitBtn = findViewById(R.id.forgotPasswordSubmitBtn);
        submitBtn.setOnClickListener(this::submitBtnOnClick);
    }



    private void backImageOnClick(View view) {
        finish();
    }

    private void submitBtnOnClick(View view) {
        String email = emailInput.getText().toString();
        if(isNotNull(email)) {
            if (validateEmail(email)) {
                try {
                    invokeWS(email);
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else {
                showToast(this, "Моля вкарайте валиден имейл");
            }
        }else {
            showToast(this, "Моля вкарайте имейл");
        }
    }

    private void invokeWS(String email) throws JSONException, UnsupportedEncodingException {
        prgDialog.show();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.190:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RequestUser requestUser = retrofit.create(RequestUser.class);

        Call<Boolean> call = requestUser.check_email(email);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                prgDialog.hide();
                if(response.body() == Boolean.TRUE){
                    Intent intent = new Intent(ForgotPasswordActivity.this, NewPasswordActivity.class);
                    intent.putExtra(EMAIL_EXTRA, email);
                    startActivity(intent);
                } else {
                    showToast(ForgotPasswordActivity.this, "USER NOT EXISTS");
                }

            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {

            }
        });
    }
}