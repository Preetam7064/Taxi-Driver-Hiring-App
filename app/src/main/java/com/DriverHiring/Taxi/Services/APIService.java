package com.DriverHiring.Taxi.Services;


import com.DriverHiring.Taxi.Notifications.MyResponse;
import com.DriverHiring.Taxi.Notifications.Sender;
import com.DriverHiring.Taxi.Notifications.SenderMessageForRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA1E5jKB8:APA91bF7-Uc0mwaUi_09fMIlF59TZHCLsdqMql8trXqmz7_vSWHxxWuCej5TA21ArvXqnUZJ3-1364mGa00v1x2xWrhkNvnpfPf2nWnpCQu9pXWuHjb0gKW6xQQXQEOdJ0B0MiR6lOlA"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

    @POST("fcm/send")
    Call<MyResponse> sendRequestNotification(@Body SenderMessageForRequest body);
}
