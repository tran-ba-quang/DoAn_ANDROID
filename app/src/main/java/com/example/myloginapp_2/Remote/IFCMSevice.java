package com.example.myloginapp_2.Remote;

import com.example.myloginapp_2.Model.FCMResponse;
import com.example.myloginapp_2.Model.FCMSendData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMSevice {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAIZTRk9k:APA91bF4RuW-9-bqwxHnqkcmgXzBALEuykjgC-MARJvFy0of1xp5JqvAmjFnouGFZLkHckqx43yLdNkNptMMnRlk0JAZrqCes7nMGLXNW5Z_OGLHJsVqjJmmGmXe0lcpEpYfcpliPK6G"
    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNOtification(@Body FCMSendData body);
}
