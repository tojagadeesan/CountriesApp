package com.example.countries.webservice


import com.example.countries.utils.URLS
import com.example.countries.model.Country
import com.example.countries.model.WeatherReport
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object ApiClient {

    private val client = OkHttpClient
        .Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URLS.COUNTRIES_API)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(CountriesApi::class.java)

    fun buildService(): CountriesApi {
        return retrofit
    }
}

object WeatherClient {
    private val client = OkHttpClient
        .Builder()
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URLS.WEATHER_API)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(CountriesApi::class.java)

    fun buildService(): CountriesApi {
        return retrofit
    }
}

interface CountriesApi {

    @GET("all")
    fun getAllCountries(): Single<MutableList<Country>>

    @GET("weather")
    fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String
    ): Single<WeatherReport>
}