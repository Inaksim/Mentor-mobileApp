package com.mentor.activity;

import static com.mentor.utils.Utils.BASE_URL;
import static com.mentor.utils.Utils.COURSE_JSON_EXTRA;
import static com.mentor.utils.Utils.CURRENT_USER_EXTRA;
import static com.mentor.utils.Utils.showToast;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mentor.R;
import com.mentor.dto.form.RequestFav;
import com.mentor.model.Course;
import com.mentor.model.User;
import com.mentor.requests.RequestUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceDetailActivity extends AppCompatActivity {


    private TextView courseName;

    private TextView courseNameCard;

    private TextView courseSubtitle;

    private TextView textDescription;

    private ConstraintLayout cardView;

    private ImageView backImage;

    private ImageView favoriteImage;

    private String courseTitle;

    private ProgressDialog progressDialog;
    private WebView webView;

    private boolean isFavorite;
    private ImageButton btn;
    private TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        String courseJSON = getIntent().getStringExtra(COURSE_JSON_EXTRA);
        Course course = new Gson().fromJson(courseJSON, new TypeToken<Course>() {}.getType());


        progressDialog = new ProgressDialog(this);

        courseName = findViewById(R.id.courseNamePanel);
        courseNameCard = findViewById(R.id.courseNameCard);
        courseSubtitle = findViewById(R.id.courseSubtitle);
        textDescription = findViewById(R.id.textDescription);
        cardView = findViewById(R.id.cardBackground);
        backImage = findViewById(R.id.courseDescriptionBackImage);
        favoriteImage = findViewById(R.id.courseDescriptionFavouriteImage);
        webView = findViewById(R.id.webView);
        btn = (ImageButton) findViewById(R.id.textView17);;
        textView = findViewById(R.id.textView12);

        isFavorite = course.isFav();
        setFavoriteState();

        favoriteImage.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            favoriteImage.setImageResource(isFavorite ? R.drawable.ic_heart : R.drawable.ic_unliked_without_bg);

            changeFavoriteStatus(isFavorite, course.getCourse_id());
        });

        backImage.setOnClickListener(v -> finish());

        courseName.setText(course.getTitle());
        courseTitle = course.getTitle();

        courseNameCard.setText(course.getTitle());

        courseSubtitle.setText(String.valueOf(course.getMembers()));

        textDescription.setText(course.getDescription());

        textView.setText(course.getTitle());

        String base64Image = course.getPicture();
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        cardView.setBackground(drawable);


        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                webView.loadUrl(course.getLink());
            }
        });


    }

    private void changeFavoriteStatus(boolean isFavorite, Long courseId) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RequestUser requestUser = retrofit.create(RequestUser.class);
        Intent intent = getIntent();
        String userString = intent.getStringExtra(CURRENT_USER_EXTRA);
        User user = gson.fromJson(userString, User.class);
        RequestFav req = new RequestFav(courseId, user.getMail());
        Call<String> call = requestUser.makeFavorite(req);
        Call<String> call2 = requestUser.removeFavorite(req);
        if (isFavorite) {
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    progressDialog.hide();
                    if (response.code() == 401) {
                        showToast(PlaceDetailActivity.this, "Невалидни потребителски данни");
                    } else if (response.code() == 404) {
                        showToast(PlaceDetailActivity.this, "Страницата не е намерена");
                    } else if (response.code() == 500) {
                        showToast(PlaceDetailActivity.this, "Сървърна грешка");
                    } else {
                        startActivity(new Intent(PlaceDetailActivity.this, LoginActivity.class));
                    }
                    showToast(PlaceDetailActivity.this, "Курсът добавен в любимите");
                }


                @Override
                public void onFailure(Call<String> call, Throwable throwable) {

                }
            });
        } else {
            call2.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 401) {
                        showToast(PlaceDetailActivity.this, "Невалидни потребителски данни");
                    } else if (response.code() == 404) {
                        showToast(PlaceDetailActivity.this, "Страницата не е намерена");
                    } else if (response.code() == 500) {
                        showToast(PlaceDetailActivity.this, "Сървърна грешка");
                    } else {
                        showToast(PlaceDetailActivity.this, "Курсът е отстранен от любимите");

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {

                }
            });
        }
    }

    private void setFavoriteState(){
        favoriteImage.setImageResource(isFavorite ? R.drawable.ic_heart : R.drawable.ic_unliked_without_bg);
    }
}