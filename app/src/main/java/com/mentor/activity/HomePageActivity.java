package com.mentor.activity;

import static com.mentor.utils.Utils.CURRENT_USER_EXTRA;
import static com.mentor.utils.Utils.SEARCH_EXTRA;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
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
import com.mentor.model.Course;
import com.mentor.model.User;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class HomePageActivity extends AppCompatActivity {
    private  RecyclerView catalogRecycler;

    private RecyclerView locationRecycler;

    private ProgressDialog progressDialog;

    private Gson gson;

    private TextView greetingText;

    private SearchView searchView;

    private ImageView noContentImage;

    public static final String GREETING_TEXT = "Здравейте, {0}!";

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_home_page);

        gson = new Gson();

        progressDialog = new ProgressDialog(this);

        Intent currentIntent = getIntent();
        Bundle currentBundle = currentIntent.getExtras();
        String currentUserString = currentBundle.getString(CURRENT_USER_EXTRA);
        User currentUser = gson.fromJson(currentUserString, new TypeToken<User>(){}.getType());

        noContentImage = findViewById(R.id.noContentImage);
        greetingText = findViewById(R.id.homePageGreetingTextView);
        greetingText.setText(MessageFormat.format(GREETING_TEXT, currentUser.getName()));

        searchView = findViewById(R.id.homeSearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        invokeWS();
    }


    private void invokeWS() {
        progressDialog.show();


    }

    private void setCurseRecycler(List<Course> courses) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);

        catalogRecycler = findViewById(R.id.categoryRecycler);
        catalogRecycler.setLayoutManager(layoutManager);



    }
}
