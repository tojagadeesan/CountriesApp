package com.example.countries.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.countries.R
import com.example.countries.utils.*
import com.example.countries.viewmodel.CountriesViewModel
import kotlinx.android.synthetic.main.fragment_country_list.*
import java.lang.StringBuilder
import java.text.DecimalFormat

class CountryListFragment : Fragment(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var viewModel: CountriesViewModel
    private var countriesCountriesAdapter: CountriesAdapter? = null
    private var spanCount: Int = 2
    private val locationPermissionCode = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_country_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.app_name)
        viewModel = ViewModelProviders.of(this).get(CountriesViewModel::class.java)
        viewModel.onCreate()
        checkSpanCount()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                countriesCountriesAdapter?.filter(query)
                return true;
            }

            override fun onQueryTextChange(query: String?): Boolean {
                countriesCountriesAdapter?.filter(query)
                return true
            }

        })
        getCountries ()
        getLocation()
    }

    private fun getCountries(isRefreshData: Boolean = false){

        viewModel.getCountries(isRefreshData)?.observe(this, Observer {
            if(it.isNullOrEmpty() && !CountryApplication.INSTANCE.isNetworkAvailable()){
                not_network_layout.visibility=View.VISIBLE
                no_network_btn_retry.setOnClickListener{
                    if(CountryApplication.INSTANCE.isNetworkAvailable()){
                        not_network_layout.visibility= View.GONE
                        getCountries(true)
                    }
                }
            }else {
                if (!it.isNullOrEmpty()) {
                    countriesCountriesAdapter = CountriesAdapter(
                            context!!,
                            it.toMutableList(),
                            spanCount
                    ) {
                        var args: Bundle = Bundle();
                        args.putSerializable(BundleKeys.COUNTRY, it)
                        findNavController().navigate(R.id.countryDetailFragment, args)
                    }
                    recyclerview?.layoutManager =
                            StaggeredGridLayoutManager(spanCount, LinearLayoutManager.VERTICAL)
                    recyclerview?.adapter = countriesCountriesAdapter
                    shimmer_layout.visibility = View.GONE
                    rootLayout.visibility = View.VISIBLE
                    list_layout.visibility = View.VISIBLE
                }
            }
        })
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        checkSpanCount()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onLocationChanged(p0: Location) {
        viewModel.getWeather(
            p0.latitude.toString(),
            p0.longitude.toString(),
            (context!!.applicationContext as CountryApplication).isNetworkAvailable()
        ).observe(this, {

            val df2 = DecimalFormat("#.##")
            weather_title.text = getString(
                R.string.temperature_format,
                df2.format(it.main.temp - 273.15).toString(),
                it.name
            )
            weather_report.text = it.weather[0].description
            weather_icon.visibility = View.VISIBLE
            val iconUrl = StringBuilder(URLS.ICON_API).append(it.weather[0].icon).append(UtilConstants.IMAGE_FORMAT).toString()
            GlideApp.with(this)
                .load( iconUrl)
                .centerInside()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        weather_title.visibility = View.VISIBLE
                        weather_report.visibility = View.VISIBLE
                        return false

                    }

                })
                .into(weather_icon)

        })
        locationManager.removeUpdates(this)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

    private fun getLocation() {
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            showDialog()
        } else {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (null == location) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5f, this)
            } else {
                onLocationChanged(location)
            }
        }
    }

    private fun checkSpanCount() {
        spanCount =
            if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
        recyclerview?.layoutManager =
            StaggeredGridLayoutManager(spanCount, LinearLayoutManager.VERTICAL)
    }

    fun showDialog(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.location_dialog_title))
        builder.setMessage(getString(R.string.location_dialog_detail))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
        }
        builder.show()
    }

}