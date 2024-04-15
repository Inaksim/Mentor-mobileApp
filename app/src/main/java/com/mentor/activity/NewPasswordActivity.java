package com.mentor.activity;

import static com.mentor.utils.Utils.EMAIL_EXTRA;
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
import com.mentor.dto.form.ResetPasswordForm;
import com.mentor.dto.view.UserView;
import com.mentor.requests.RequestUser;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewPasswordActivity extends AppCompatActivity {

    private ImageView backImage;

    private TextInputEditText newPasswordInput;

    private TextInputEditText confirmPasswordInput;

    private Button saveBtn;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        progressDialog = new ProgressDialog(this);

        backImage = findViewById(R.id.newPasswordBackImage);
        backImage.setOnClickListener(this::backImageOnClick);

        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.newConfirmPasswordInput);

        saveBtn = findViewById(R.id.newPasswordSaveBtn);
        saveBtn.setOnClickListener(this::saveBtnOnClick);
    }

    private void saveBtnOnClick(View view) {
        String newPassword = newPasswordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        if (isNotNull(newPassword) && isNotNull(confirmPassword)) {
            if (newPassword.equals(confirmPassword)) {
                if (validatePassword(newPassword)) {
                    invokeWS(newPassword);
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

    private void backImageOnClick(View view) {
        finish();
    }

    private void invokeWS(String newPassword) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.190:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RequestUser requestUser = retrofit.create(RequestUser.class);
        progressDialog.show();
        ResetPasswordForm form = new ResetPasswordForm(getIntent().getStringExtra(EMAIL_EXTRA), newPassword);

        Call<UserView> call = requestUser.resetPassword(form);
        call.enqueue(new Callback<UserView>() {
            @Override
            public void onResponse(Call<UserView> call, Response<UserView> response) {
                progressDialog.hide();
                startActivity(new Intent(NewPasswordActivity.this, LoginActivity.class));
            }

            @Override
            public void onFailure(Call<UserView> call, Throwable throwable) {
                progressDialog.hide();
                showToast(NewPasswordActivity.this, throwable.getMessage());
            }
        });

    }

}
