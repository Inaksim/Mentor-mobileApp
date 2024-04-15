package com.mentor.activity;

import static com.mentor.utils.Utils.CURRENT_USER_EXTRA;
import static com.mentor.utils.Utils.SEARCH_EXTRA;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mentor.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mentor.adapter.CategoryAdapter;
import com.mentor.adapter.CourseAdapter;

import com.mentor.model.Category;
import com.mentor.model.Course;
import com.mentor.model.User;
import com.mentor.requests.RequestUser;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.CheckedOutputStream;


import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePageActivity extends AppCompatActivity {
    private RecyclerView catalogRecycler;

    private RecyclerView courseRecycler;

    private CourseAdapter courseAdapter;
    private CategoryAdapter categoryAdapter;



    private ProgressDialog progressDialog;

    private TextView greetingText;

    private SearchView searchView;

    private ImageView noContentImage;

    public static final String GREETING_TEXT = "Здравейте, {0}!";



    @Override
    protected void onResume() {
        super.onResume();
        updateCourses();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        Gson gson = new Gson();

        progressDialog = new ProgressDialog(this);

        Intent currentIntent = getIntent();
        Bundle currentBundle = currentIntent.getExtras();
        String currentUserString = currentBundle.getString(CURRENT_USER_EXTRA);
        User currentUser = gson.fromJson(currentUserString, new TypeToken<User>() {}.getType());

        noContentImage = findViewById(R.id.noContentImage);

        greetingText = findViewById(R.id.homePageGreetingTextView);

        greetingText.setText(MessageFormat.format(GREETING_TEXT, currentUser.getName()));

        searchView = findViewById(R.id.homeSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent searchIntent = new Intent(HomePageActivity.this, SearchActivity.class);
                String currentUserString = getIntent().getExtras().getString(CURRENT_USER_EXTRA);
                User currentUser = gson.fromJson(currentUserString, new TypeToken<User>() {}.getType());
                searchIntent.putExtra(SEARCH_EXTRA, s);
                searchIntent.putExtra(CURRENT_USER_EXTRA, currentUserString);
                startActivity(searchIntent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        updateCourses();
        setCourseRecycler(new LinkedList<>());

        invokeWS();
    }



    private void updateCourses() {
        progressDialog.show();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.190:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RequestUser requestUser = retrofit.create(RequestUser.class);

        Intent currentIntent = getIntent();
        Bundle currentBundle = currentIntent.getExtras();
        String currentUserString = currentBundle.getString(CURRENT_USER_EXTRA);
        User currentUser = gson.fromJson(currentUserString, new TypeToken<User>() {}.getType());
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("currentUserEmail", currentUser.getMail());
        Call<List<Course>> call = requestUser.getFollows(currentUser.getId());

        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                progressDialog.hide();
                List<Course> courses = gson.fromJson(gson.toJson(response.body()), new TypeToken<List<Course>>() {}.getType());
                courseAdapter.setCourse(courses);
                courseAdapter.notifyDataSetChanged();

                if(courses.isEmpty()) {
                    noContentImage.setVisibility(View.VISIBLE);
                    courseRecycler.setVisibility(View.GONE);
                } else {
                    noContentImage.setVisibility(View.GONE);
                    courseRecycler.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable throwable) {

            }
        });

    }

    private void invokeWS() {
        progressDialog.show();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.190:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RequestUser requestUser = retrofit.create(RequestUser.class);

        String str = getIntent().getStringExtra(CURRENT_USER_EXTRA);
        User user = gson.fromJson(str, new TypeToken<User>() {}.getType());
        Call<List<Category>> call = requestUser.getCategory(user.getId());


        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                progressDialog.hide();
//                List<Category> fetchedCategory  = gson.fromJson(gson.toJson(response.body()), new TypeToken<List<Category>>() {}.getType());
                List<Category> fetchedCategory = response.body();
                setCategoryRecycler(fetchedCategory);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable throwable) {
            }
        });

    }

    private void setCategoryRecycler(List<Category> categories) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        catalogRecycler = findViewById(R.id.categoryRecycler);
        catalogRecycler.setLayoutManager(layoutManager);
        Gson gson = new Gson();
        String currentUserString = getIntent().getExtras().getString(CURRENT_USER_EXTRA);

        categoryAdapter = new CategoryAdapter(this, categories, currentUserString);
        catalogRecycler.setAdapter(categoryAdapter);
    }

    private void setCourseRecycler(List<Course> courses) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        courseRecycler = findViewById(R.id.locationRecycler);
        courseRecycler.setLayoutManager(layoutManager);


        Gson gson = new Gson();
        String currentUserString = getIntent().getExtras().getString(CURRENT_USER_EXTRA);
        User currentUser = gson.fromJson(currentUserString, new TypeToken<User>() {}.getType());

        courseAdapter = new CourseAdapter(this, courses, currentUserString);
        courseRecycler.setAdapter(courseAdapter);
    }

}
