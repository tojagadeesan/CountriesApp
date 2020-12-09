package com.example.countries.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.countries.R

class MainActivity : AppCompatActivity(),TextString{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this,jagString("My name is "),Toast.LENGTH_SHORT).show()
        Toast.makeText(this,suffixString("My name is "),Toast.LENGTH_SHORT).show()
        setContentView(R.layout.activity_main)
    }
}