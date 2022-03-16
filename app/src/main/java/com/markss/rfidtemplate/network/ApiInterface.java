package com.markss.rfidtemplate.network;

import com.markss.rfidtemplate.modules.login.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers("Content-Type: application/json")

    @POST("WELSPUN_Login")
    Call<UserModel> loginUser(@Body UserModel userModel);
}
