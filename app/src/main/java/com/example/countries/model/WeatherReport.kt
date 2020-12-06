package com.example.countries.model

import com.google.gson.annotations.SerializedName

data class WeatherReport(

    @SerializedName("base")
    val base: String,

    @SerializedName("clouds")
    val clouds: Clouds,

    @SerializedName("cod")
    val cod: Int,

    @SerializedName("coord")
    val coord: Coord,

    @SerializedName("dt")
    val dt: Int,

    @SerializedName("id")
    val id: Int,

    @SerializedName("main")
    val main: Main,

    @SerializedName("name")
    val name: String,

    @SerializedName("sys")
    val sys: Sys,

    @SerializedName("weather")
    val weather: List<Weather>,

    @SerializedName("wind")
    val wind: Wind
)

data class Weather(

    @SerializedName("description")
    val description: String,

    @SerializedName("icon")
    val icon: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("main")
    val main: String
)

data class Sys(

    @SerializedName("country")
    val country: String,

    @SerializedName("message")
    val message: Double,

    @SerializedName("sunrise")
    val sunrise: Int,

    @SerializedName("sunset")
    val sunset: Int
)

data class Main(

    @SerializedName("grnd_level")
    val grnd_level: Double,

    @SerializedName("humidity")
    val humidity: Int,

    @SerializedName("pressure")
    val pressure: Double,

    @SerializedName("sea_level")
    val sea_level: Double,

    @SerializedName("temp")
    val temp: Double,

    @SerializedName("temp_max")
    val temp_max: Double,

    @SerializedName("temp_min")
    val temp_min: Double
)

data class Coord(

    @SerializedName("lat")
    val lat: Double,

    @SerializedName("lon")
    val lon: Double
)

data class Clouds(

    @SerializedName("all")
    val all: Int
)


data class Wind(
    @SerializedName("deg")
    val deg: Int,

    @SerializedName("speed")
    val speed: Double
)