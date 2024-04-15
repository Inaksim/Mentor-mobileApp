package com.mentor.adapter;


import static android.content.Intent.getIntent;
import static com.mentor.utils.Utils.COURSE_JSON_EXTRA;
import static com.mentor.utils.Utils.CURRENT_USER_EXTRA;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mentor.R;
import com.mentor.activity.HomePageActivity;
import com.mentor.activity.PlaceDetailActivity;
import com.mentor.model.Course;
import com.mentor.model.User;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private Context context;

    private List<Course> courses;

    private List<Course> allCourse;
    private String currentUserEmail;

    public CourseAdapter(Context context, List<Course> courses,  String currentUserEmail) {
        this.context = context;
        this.courses = courses;
        this.currentUserEmail = currentUserEmail;
    }
    public CourseAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
    }

    public List<Course> getCourses() {
        return courses;
    }
    public List<Course> getAllCourses() {
        return allCourse;
    }

    public void setCourse(List<Course> courses) {
        this.courses = courses;
    }
    public void setAllCourse(List<Course> allCourse) {
        this.allCourse = allCourse;
    }



    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View courseItems;
        if (context instanceof HomePageActivity) {
            courseItems = LayoutInflater.from(context).inflate(R.layout.fav_category_design, parent, false);
        }else {
            courseItems = LayoutInflater.from(context).inflate(R.layout.all_category_design, parent, false);
        }
        return new CourseViewHolder(courseItems);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Gson gson = new Gson();
        Course course = courses.get(position);
        holder.courseName.setText(course.getTitle());
        holder.courseSub.setText(String.valueOf(course.getMembers()));

        if(!(context instanceof HomePageActivity)) {
            holder.courseSub.setText(String.valueOf(course.getMembers()));

        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlaceDetailActivity.class);
            String jsonCourse = new Gson().toJson(course);
            intent.putExtra(COURSE_JSON_EXTRA, jsonCourse);
            intent.putExtra(CURRENT_USER_EXTRA, currentUserEmail);

            ((Activity) context).startActivityForResult(intent, 1);
        });
    }
    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static final class CourseViewHolder extends RecyclerView.ViewHolder {

        private TextView courseName;

        private  TextView courseSub;
        private ConstraintLayout cardBackground;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);

            if (itemView.getContext() instanceof HomePageActivity) {
                courseName = itemView.findViewById(R.id.locationNameCard);
                courseSub = itemView.findViewById(R.id.locationSubtitle);
                cardBackground = itemView.findViewById(R.id.cardBackground);
            }else {
                courseName = itemView.findViewById(R.id.allCategoryLocationName);
                courseSub = itemView.findViewById(R.id.allCategoriesLocationSubtitle);
                cardBackground = itemView.findViewById(R.id.allCategoriesLocationImage);
            }
        }
    }

}
