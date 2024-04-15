package com.mentor.activity;

import static com.mentor.utils.Utils.COURSE_JSON_EXTRA;
import static com.mentor.utils.Utils.CURRENT_USER_EXTRA;
import static com.mentor.utils.Utils.showToast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mentor.R;
import com.mentor.dto.form.RequestFav;
import com.mentor.model.Course;
import com.mentor.model.User;
import com.mentor.requests.RequestUser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceDetailActivity extends AppCompatActivity {

    private RecyclerView photoGalleryRecyclerView;

    private TextView locationName;

    private TextView locationNameCard;

    private TextView locationSubtitle;

    private TextView textDescription;

    private ConstraintLayout cardView;

    private ImageView backImage;

    private ImageView favoriteImage;

    private String locationTitle;

    private ProgressDialog progressDialog;
    private WebView webView;

    private boolean isFavorite;
    private ImageButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        String locationJSON = getIntent().getStringExtra(COURSE_JSON_EXTRA);
        Course course = new Gson().fromJson(locationJSON, new TypeToken<Course>() {}.getType());


        progressDialog = new ProgressDialog(this);

        locationName = findViewById(R.id.locationNamePanel);
        locationNameCard = findViewById(R.id.locationNameCard);
        locationSubtitle = findViewById(R.id.locationSubtitle);
        textDescription = findViewById(R.id.textDescription);
        cardView = findViewById(R.id.cardBackground);
        backImage = findViewById(R.id.locationDescriptionBackImage);
        favoriteImage = findViewById(R.id.locationDescriptionFavouriteImage);
        webView = findViewById(R.id.webView);
        btn = (ImageButton) findViewById(R.id.textView17);;

        isFavorite = course.isFav();
        setFavoriteState();

        favoriteImage.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            favoriteImage.setImageResource(isFavorite ? R.drawable.ic_heart : R.drawable.ic_unliked_without_bg);

            changeFavoriteStatus(isFavorite, course.getCourse_id());
        });

        backImage.setOnClickListener(v -> finish());

        locationName.setText(course.getTitle());
        locationTitle = course.getTitle();

        locationNameCard.setText(course.getTitle());

        locationSubtitle.setText(String.valueOf(course.getMembers()));

        textDescription.setText(course.getDescription());


        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                webView.loadUrl(course.getLink());
            }
        });


//        Intent intent = getIntent();
//        String userEmail = intent.getStringExtra("CURRENT_USER_EXTRA");
//
//        RequestFav req = new RequestFav(course.getCourse_id(), userEmail);

//        inwokeWS(req);
    }

    private void changeFavoriteStatus(boolean isFavorite, Long locationId) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.190:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RequestUser requestUser = retrofit.create(RequestUser.class);



        Intent intent = getIntent();
        String userString = intent.getStringExtra(CURRENT_USER_EXTRA);
        User user = gson.fromJson(userString, User.class);
        RequestFav req = new RequestFav(locationId, user.getMail());

        Call<String> call = requestUser.makeFavorite(req);
        Call<String> call2 = requestUser.removeFavorite(req);
//        Call<List<Course>> call3 = requestUser.getFavorite(req);

//        progressDialog.show();
        if (isFavorite) {
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
//                    progressDialog.hide();
                    showToast(PlaceDetailActivity.this, "Course added to favorite");
//                    Intent homePage = new Intent(PlaceDetailActivity.this, HomePageActivity.class);
//                    startActivity(homePage);
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {

                }
            });
        } else {
            call2.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
//                    progressDialog.hide();
                    showToast(PlaceDetailActivity.this, "Course removed from favorite");
//                    Intent homePage = new Intent(PlaceDetailActivity.this, HomePageActivity.class);
//                    startActivity(homePage);

                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {

                }
            });
        }
    }

//    private void inwokeWS(RequestFav req) {
//
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.0.190:8080")
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//        RequestUser requestUser = retrofit.create(RequestUser.class);
//        Call<String> call = requestUser.getFavorite(req);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                isFavorite = true;
//                setFavoriteState();
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable throwable) {
//
//            }
//        });
//
//    }
    private void setFavoriteState(){
        favoriteImage.setImageResource(isFavorite ? R.drawable.ic_heart : R.drawable.ic_unliked_without_bg);
    }
}