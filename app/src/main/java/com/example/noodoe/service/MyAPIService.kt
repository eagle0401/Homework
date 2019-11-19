package com.example.noodoe.service

import com.example.noodoe.data.model.LoggedInUser
import com.example.noodoe.data.model.TimeZone
import com.example.noodoe.data.model.UpdateTimeAt
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface MyAPIService {

    @FormUrlEncoded
    @Headers("X-Parse-Application-Id:vqYuKPOkLQLYHhk4QTGsGKFwATT4mBIGREI2m8eD","X-Parse-REST-API-Key:\"\"")
    @POST("api/login")
    fun login(@Field("username") username: String, @Field("password") password: String): Observable<LoggedInUser>

    @Headers("X-Parse-Application-Id:vqYuKPOkLQLYHhk4QTGsGKFwATT4mBIGREI2m8eD","X-Parse-REST-API-Key:\"\"")
    @PUT("api/users/{objectId}")
    fun modifyTimeZone(@Path("objectId") objectId: String, @Header("X-Parse-Session-Token") token: String, @Body timezone: TimeZone): Observable<UpdateTimeAt>

    class RetrofitManager {

        val api: MyAPIService
        var loggedInUser: LoggedInUser = LoggedInUser("","","",false,"","","",0,"")

        init {
            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://watch-master-staging.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

            api = retrofit.create(MyAPIService::class.java)
        }

        companion object {
            val instance = RetrofitManager()
        }
    }

}