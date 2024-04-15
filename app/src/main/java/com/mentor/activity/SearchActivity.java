package com.mentor.activity;

import static com.mentor.utils.Utils.CURRENT_USER_EXTRA;
import static com.mentor.utils.Utils.SEARCH_EXTRA;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mentor.R;
import com.mentor.adapter.CourseAdapter;

import com.mentor.model.Course;
import com.mentor.model.User;
import com.mentor.requests.RequestUser;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.lang.ref.ReferenceQueue;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//load courses
public class SearchActivity extends AppCompatActivity {
    private TextView searchTextView;
    private ProgressDialog progressDialog;

    private RecyclerView locationRecycler;

    private CourseAdapter courseAdapter;

    private ImageView backImage;

    private SearchView searchView;

    public static final String SEARCH_TEXT_VIEW_VALUE = "Намерихме {0} резултата за: {1}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        progressDialog = new ProgressDialog(this);
        backImage = findViewById(R.id.searchBackImage);
        backImage.setOnClickListener(v -> finish());
        searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<Course> locations = courseAdapter.getAllCourses();
                List<Course> filteredLocations = locations.stream().filter(course -> course.getTitle().toLowerCase().contains(s.toLowerCase())).collect(Collectors.toList());
                courseAdapter.setCourse(filteredLocations);
                courseAdapter.notifyDataSetChanged();
                searchTextView.setText(MessageFormat.format(SEARCH_TEXT_VIEW_VALUE, filteredLocations.size(), s));
                return false;
            }
        });

        searchTextView = findViewById(R.id.searchTextView);
        String search = getIntent().getStringExtra(SEARCH_EXTRA);

        invokeWS(search);
    }

    private void invokeWS(String search) {
        progressDialog.show();

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

        Call<List<Course>> call = requestUser.courses(user.getMail());


        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                progressDialog.hide();
//                List<Course> courses = gson.fromJson(response.body().toString(), new TypeToken<List<Course>>() {}.getType());

                List<Course> courses = response.body();
                List<Course> filteredCourse = courses.stream().filter(course -> course.getTitle().toLowerCase().contains(search.toLowerCase())).collect(Collectors.toList());
                searchTextView.setText(MessageFormat.format(SEARCH_TEXT_VIEW_VALUE, filteredCourse.size(), search));
                setLocationRecycler(filteredCourse);
                courseAdapter.setAllCourse(courses);
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable throwable) {
                progressDialog.hide();

            }
        });
    }

    private void setLocationRecycler(List<Course> locations) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        locationRecycler = findViewById(R.id.searchLocationsRecycler);
        locationRecycler.setLayoutManager(layoutManager);

        Gson gson = new Gson();
        String currentUserString = getIntent().getExtras().getString(CURRENT_USER_EXTRA);

        courseAdapter = new CourseAdapter(this, locations, currentUserString);
        locationRecycler.setAdapter(courseAdapter);
    }
}