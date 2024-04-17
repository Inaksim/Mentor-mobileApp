package com.mentor.requests;



import com.mentor.dto.form.RequestFav;
import com.mentor.dto.form.ResetPasswordForm;
import com.mentor.dto.form.SignInForm;
import com.mentor.dto.form.SignUpForm;
import com.mentor.dto.view.UserView;
import com.mentor.model.Category;
import com.mentor.model.Course;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestUser {

    //Authentication requests
    @POST("/auth/sign-in")
    Call<UserView> signInPost(@Body SignInForm form);

    @POST("/auth/sign-up")
    Call<UserView> signUpPost(@Body SignUpForm form);

    @PUT("/auth/reset_password")
    Call<UserView> resetPassword(@Body ResetPasswordForm form);

    @GET("/auth/check_email")
    Call<Boolean> check_email(@Query("email") String email);

    //Course Request

    @GET("/course/get-all")
    Call<List<Course>> courses(@Query("email") String email);

    @GET("/course/follows/{id}")
    Call<List<Course>> getFollows(@Path("id") Long id);

    @POST("/course/add-favorite")
    Call<String> makeFavorite(@Body RequestFav requestFav);

    @POST("/course/remove-favorite")
    Call<String> removeFavorite(@Body RequestFav requestFav);


    // Category request

    @GET("/category/get-all/{id}")
    Call<List<Category>> getCategory(@Path("id") Long id);

    @GET("/category/get/{id}")
    Call<Category> getCategoryById(@Path("id") Long id, @Query("userId") Long userId);


}
