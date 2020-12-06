package com.example.countries.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.countries.model.Country
import com.example.countries.model.WeatherReport
import com.example.countries.utils.UtilConstants
import com.example.countries.webservice.ApiClient
import com.example.countries.webservice.WeatherClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


object CountriesRepo {

    private var countriesResponse: MutableLiveData<MutableList<Country>> = MutableLiveData()
    private var weatherReport: MutableLiveData<WeatherReport> = MutableLiveData()
    private var database: CountryDatabase? = CountryDatabase.getDatabase()

    fun getCountries(): MutableLiveData<MutableList<Country>> {
        CompositeDisposable().add(
            ApiClient.buildService().getAllCountries()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<MutableList<Country>>() {
                    override fun onSuccess(response: MutableList<Country>) {
                        countriesResponse.postValue(response)
                        GlobalScope.launch {
                            database?.countryDao()?.insertAll(response)
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.e("getCountries", "onError", e)
                    }
                })
        )
        return countriesResponse
    }

    fun getWeather(lat: String, lon: String): MutableLiveData<WeatherReport> {
        CompositeDisposable().add(
            WeatherClient.buildService().getWeather(lat, lon, UtilConstants.WEATHER_API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<WeatherReport>() {
                    override fun onSuccess(response: WeatherReport) {
                        weatherReport.postValue(response)
                    }

                    override fun onError(e: Throwable) {
                        Log.e("getWeather", "onError", e)
                    }
                })
        )
        return weatherReport
    }

    fun getCountryList(): LiveData<List<Country>>? {
        return database?.countryDao()?.getAll()
    }
}