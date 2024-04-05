package com.mentor.requests;

import com.mentor.dto.form.SignInForm;
import com.mentor.dto.view.UserView;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestUser {
    @GET("/auth/user/{id}")
    Call<UserView> getUser(@Path("id") Long id);

    @GET("/auth/test")
    Call<String> test();

    @POST("/auth/sign-in")
    Call<UserView> signInPost(@Body SignInForm sign);
}
