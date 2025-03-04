package com.mentor.activity;

import static com.mentor.utils.Utils.FIRST_NAME_EXTRA;
import static com.mentor.utils.Utils.LAST_NAME_EXTRA;
import static com.mentor.utils.Utils.isNotNull;
import static com.mentor.utils.Utils.showToast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.mentor.R;

public class RegisterNameActivity  extends AppCompatActivity {
    private ImageView backImage;

    private TextInputEditText firstNameInput;

    private TextInputEditText lastNameInput;

    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);

        backImage = findViewById(R.id.regNameBackImage);
        backImage.setOnClickListener(this::backImageOnClick);

        firstNameInput = findViewById(R.id.regFirstNameInput);
        lastNameInput = findViewById(R.id.regLastNameInput);

        saveBtn = findViewById(R.id.regNameSaveBtn);
        saveBtn.setOnClickListener(this::saveBtnOnClick);
    }

    private void saveBtnOnClick(View view) {
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();

        if (isNotNull(firstName) && isNotNull(lastName)) {
            Intent intent = new Intent(this, RegisterEmailActivity.class);
            intent.putExtra(FIRST_NAME_EXTRA, firstName);
            intent.putExtra(LAST_NAME_EXTRA, lastName);
            startActivity(intent);
        }else {
            showToast(this, "Попълнете всички полета");
        }
    }

    private void backImageOnClick(View view) {
        finish();
    }

}
