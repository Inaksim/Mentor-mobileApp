package com.mentor.adapter;

import static com.mentor.utils.Utils.CATEGORY_ID_EXTRA;
import static com.mentor.utils.Utils.CURRENT_USER_EXTRA;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mentor.R;
import com.mentor.activity.CategoryActivity;
import com.mentor.activity.HomePageActivity;
import com.mentor.model.Category;

import android.util.Base64;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;

    private List<Category> categories;
    private String currentUserEmail;

    public CategoryAdapter(Context context, List<Category> categories, String currentUserEmail) {
        this.context = context;
        this.categories = categories;
        this.currentUserEmail = currentUserEmail;
    }
    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View categoryItems = LayoutInflater.from(context).inflate(R.layout.category_design, parent, false);
        return new CategoryViewHolder(categoryItems);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.categoryName.setText(categories.get(position).getName());
        String base64Image = categories.get(position).getPicture();
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.categoryPicture.setImageBitmap(decodedByte);

        holder.itemView.setOnClickListener(v -> {
            if (context instanceof HomePageActivity || context instanceof CategoryActivity) {
                Category category = categories.get(position);
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra(CURRENT_USER_EXTRA, currentUserEmail);

                intent.putExtra(CATEGORY_ID_EXTRA, category.getId());
                ((Activity) context).startActivityForResult(intent, 1);
            }else {
                ((Activity) context).getIntent().putExtra(CATEGORY_ID_EXTRA, categories.get(position).getId());
                CourseAdapter courseAdapter = ((CategoryActivity)context).getCourseAdapter();
                courseAdapter.setAllCourse(categories.get(position).getCourses());
                courseAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static final class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryName;

        private ImageView categoryPicture;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
            categoryPicture = itemView.findViewById(R.id.categoryPicture);
        }
    }

}
