package com.flavienmedina.mytodolist.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface TodoApiService {
    @GET("tasks")
//    @Headers("Content-Type: application/json")
    fun getTasks(): Deferred<List<Task>>

    @POST("tasks")
    @Headers("Content-Type: application/json")
    fun createTasks(@Body task: Task): Deferred<Task>

    @POST("tasks")
    @Headers("Content-Type: application/json")
    fun deleteTasks(): Deferred<Task>

    @GET("checked tasks")
//    @Headers("Content-Type: application/json")
    fun checkedTasks(): Deferred<Task>

    @GET("unChecked tasks")
//    @Headers("Content-Type: application/json")
    fun unCheckedTasks(): Deferred<Task>
}


object TodoApiFactory {

    private const val BASE_URL = "https://beta.todoist.com/API/v8/"
    private const val TOKEN = "f5e82dc01fab7ecadc0e3a7fa1892e514d2f43b1"

    // Create OkHttpClient object
    // Each request made with my Retrofit instance, the token will be add
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $TOKEN")
                .build()
            chain.proceed(newRequest)
        }.build()

    // Create Moshi object
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Create Retrofit object
    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

    val retrofitService: TodoApiService by lazy {
        retrofit.create(TodoApiService::class.java)
    }
}