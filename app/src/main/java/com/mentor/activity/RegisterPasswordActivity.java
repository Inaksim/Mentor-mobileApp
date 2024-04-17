package com.mentor.activity;

import static com.mentor.utils.Utils.BASE_URL;
import static com.mentor.utils.Utils.EMAIL_EXTRA;
import static com.mentor.utils.Utils.FIRST_NAME_EXTRA;
import static com.mentor.utils.Utils.LAST_NAME_EXTRA;
import static com.mentor.utils.Utils.PARENT_EXTRA;
import static com.mentor.utils.Utils.isNotNull;
import static com.mentor.utils.Utils.showToast;
import static com.mentor.utils.Utils.validatePassword;

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
import com.mentor.dto.form.SignUpForm;
import com.mentor.dto.view.UserView;
import com.mentor.requests.RequestUser;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterPasswordActivity  extends AppCompatActivity {

    private ImageView backImage;

    private TextInputEditText newPasswordInput;

    private TextInputEditText confirmPasswordInput;

    private Button saveBtn;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);

        progressDialog = new ProgressDialog(this);

        backImage = findViewById(R.id.regPasswordBackImage);
        backImage.setOnClickListener(this::backImageOnClick);

        newPasswordInput = findViewById(R.id.regPasswordInput);
        confirmPasswordInput = findViewById(R.id.regConfirmPasswordInput);

        saveBtn = findViewById(R.id.regPasswordSaveBtn);
        saveBtn.setOnClickListener(this::saveBtnOnClick);
    }

    private void saveBtnOnClick(View view) {
        String newPassword = newPasswordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        if (isNotNull(newPassword) && isNotNull(confirmPassword)) {
            if (newPassword.equals(confirmPassword)) {
                if (validatePassword(newPassword)) {
                    try {
                        invokeWS(newPassword);
                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                        showToast(this, "Възникна неочаквана грешка");
                    }
                } else {
                    showToast(this, "Паролата трябва да започва с голяма буква, да е поне 6 символа и да включва поне 1 цифра");
                }
            } else {
                showToast(this, "Паролите не съвпадат");
            }
        } else {
            showToast(this, "Попълнете всички полета");
        }
    }

    private void invokeWS(String newPassword) throws JSONException, UnsupportedEncodingException {
        String firstName = getIntent().getStringExtra(FIRST_NAME_EXTRA);
        String lastName = getIntent().getStringExtra(LAST_NAME_EXTRA);
        String email = getIntent().getStringExtra(EMAIL_EXTRA);

        SignUpForm form = new SignUpForm(firstName, lastName, email, newPassword, "STUDENT");
        progressDialog.show();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RequestUser requestUser = retrofit.create(RequestUser.class);

        Call<UserView> call = requestUser.signUpPost(form);


        call.enqueue(new Callback<UserView>() {
            @Override
            public void onResponse(Call<UserView> call, Response<UserView> response) {
                progressDialog.hide();
                Intent intent = new Intent(RegisterPasswordActivity.this, LoginActivity.class);
                intent.putExtra(EMAIL_EXTRA, email);
                intent.putExtra(PARENT_EXTRA, "RegisterPasswordActivity");
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<UserView> call, Throwable throwable) {
                progressDialog.hide();
                showToast(RegisterPasswordActivity.this, "ERROR");
            }
        });


    }


    private void backImageOnClick(View view) {
        finish();
    }
}
