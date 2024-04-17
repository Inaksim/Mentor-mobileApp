package com.mentor.activity;

import static com.mentor.utils.Utils.BASE_URL;
import static com.mentor.utils.Utils.CATEGORY_ID_EXTRA;
import static com.mentor.utils.Utils.CURRENT_USER_EXTRA;
import static com.mentor.utils.Utils.showToast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mentor.R;
import com.mentor.adapter.CategoryAdapter;
import com.mentor.adapter.CourseAdapter;
import com.mentor.model.Category;
import com.mentor.model.Course;
import com.mentor.model.User;
import com.mentor.requests.RequestUser;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryActivity extends AppCompatActivity {

    private ImageView backImage;
    private RecyclerView catalogRecycler;

    private CategoryAdapter categoryAdapter;

    private RecyclerView courseRecycler;

    private CourseAdapter courseAdapter;

    private ProgressDialog progressDialog;

    private Gson gson;



    public CourseAdapter getCourseAdapter() {
        return courseAdapter;
    }

    public void setCourseAdapter(CourseAdapter courseAdapter) {
        this.courseAdapter = courseAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        progressDialog = new ProgressDialog(this);

        gson = new Gson();

        backImage = findViewById(R.id.categoriesBackImage);

        backImage.setOnClickListener(v ->{
            setResult(1);
            finish();
        });

        setCategoryRecycler(new LinkedList<>());
        setCourseRecycler(new LinkedList<>());


        Long categoryId = getIntent().getLongExtra(CATEGORY_ID_EXTRA, 0);

        invokeWS(categoryId);
    }

    private void invokeWS(Long categoryId) {
        progressDialog.show();

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
        Call<List<Category>> call = requestUser.getCategory(user.getId());


        Call<Category> call2 = requestUser.getCategoryById(categoryId, user.getId());

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.code() == 401) {
                    showToast(CategoryActivity.this, "Невалидни потребителски данни");
                } else if (response.code() == 404) {
                    showToast(CategoryActivity.this, "Страницата не е намерена");
                } else if (response.code() == 500) {
                    showToast(CategoryActivity.this, "Сървърна грешка");
                } else {
                    List<Category> fetchedCategories = response.body();
                    categoryAdapter.setCategories(fetchedCategories);
                    categoryAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable throwable) {
                progressDialog.hide();
            }
        });


        call2.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                progressDialog.hide();
                if(response.code() == 401) {
                    showToast(CategoryActivity.this, "Невалидни потребителски данни");
                } else if (response.code() == 404) {
                    showToast(CategoryActivity.this, "Страницата не е намерена");
                } else if (response.code() == 500) {
                    showToast(CategoryActivity.this, "Сървърна грешка");
                } else {
                    Category fetchedCategory = response.body();
                    courseAdapter.setCourse(fetchedCategory.getCourses());
                    courseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable throwable) {
                progressDialog.hide();
            }
        });

    }

    private void setCourseRecycler(LinkedList<Course> courses) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        courseRecycler = findViewById(R.id.categoryListCoursesRecycler);
        courseRecycler.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        String userString = intent.getStringExtra(CURRENT_USER_EXTRA);
        User user = gson.fromJson(userString, User.class);

        courseAdapter = new CourseAdapter(this, courses, userString);
        courseRecycler.setAdapter(courseAdapter);
    }

    private void setCategoryRecycler(LinkedList<Category> categories) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        catalogRecycler = findViewById(R.id.categoryListRecycler);
        catalogRecycler.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        String userString = intent.getStringExtra(CURRENT_USER_EXTRA);
        User user = gson.fromJson(userString, User.class);

        categoryAdapter = new CategoryAdapter(this, categories, userString);
        catalogRecycler.setAdapter(categoryAdapter);

    }
}
