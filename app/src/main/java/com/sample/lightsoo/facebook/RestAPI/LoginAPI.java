package com.sample.lightsoo.facebook.RestAPI;

import com.sample.lightsoo.facebook.Data.Message;
import com.sample.lightsoo.facebook.Data.User;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by LG on 2016-04-18.
 */
public interface LoginAPI {

    @POST("/login")
    Call<Message> login(@Body User user);

}
