package com.example.countries.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(
    tableName = "country",
    indices = [Index(
        value = ["flag"],
        unique = true
    )]
)
data class Country(
    @PrimaryKey
    @SerializedName("flag")
    val flag: String,
    @SerializedName("alpha2Code")
    val alpha2Code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("capital")
    val capital: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("subregion")
    val subregion: String,
    @SerializedName("currencies")
    val currencies: MutableList<Currency>,
    @SerializedName("languages")
    val languages: List<Language>
): Serializable

data class Currency(
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String
) : Serializable

data class Language(
    @SerializedName("name")
    val name: String,
    @SerializedName("nativeName")
    val nativeName: String
) : Serializable