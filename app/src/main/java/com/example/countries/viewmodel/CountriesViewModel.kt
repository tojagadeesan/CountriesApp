package com.example.countries.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countries.db.CountriesRepo
import com.example.countries.ui.CountryApplication
import com.example.countries.model.Country
import com.example.countries.model.WeatherReport

class CountriesViewModel : ViewModel() {

    private var countriesList: LiveData<List<Country>>? = CountriesRepo.getCountryList()
    private var weatherReport: MutableLiveData<WeatherReport> = MutableLiveData()

    fun onCreate() {
        if(CountryApplication.INSTANCE.isNetworkAvailable()){
            CountriesRepo.getCountries()
        }
    }
    fun getCountries(): LiveData<List<Country>>? {
        return countriesList
    }
    fun getWeather(
        lat: String,
        lon: String,
        isNetworkAvailable: Boolean
    ): MutableLiveData<WeatherReport> {
        if (isNetworkAvailable) {
            CountriesRepo.getWeather(lat, lon)
                .observeForever {
                    weatherReport.postValue(it)
                }
        }
        return weatherReport
    }

}