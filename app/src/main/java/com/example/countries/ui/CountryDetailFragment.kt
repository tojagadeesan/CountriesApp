package com.example.countries.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.countries.utils.GlideApp
import com.example.countries.R
import com.example.countries.model.Country
import com.example.countries.model.Currency
import com.example.countries.utils.BundleKeys
import kotlinx.android.synthetic.main.fragment_country_detail.view.*

class CountryDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_country_detail, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments.let {
            if (null != it?.get(BundleKeys.COUNTRY)) {
                view?.detail_fragment_country_flag?.let { imageView ->
                    GlideApp.with(this)
                        .asDrawable()
                        .load((it.get(BundleKeys.COUNTRY) as Country).flag)
                        .into(imageView)
                }
                view?.tv_country_name?.text = (it.get(BundleKeys.COUNTRY) as Country).name
                view?.tv_country_capital?.text = (it.get(BundleKeys.COUNTRY) as Country).capital
                view?.tv_country_region?.text = (it.get(BundleKeys.COUNTRY) as Country).region
                view?.tv_country_subr_region?.text = (it.get(BundleKeys.COUNTRY) as Country).subregion
                view?.tv_country_currencies?.text = (it.get(BundleKeys.COUNTRY) as Country).currencies.map { it.name }.toTypedArray().joinToString(separator = ", ")
                view?.tv_country_languages?.text = (it.get(BundleKeys.COUNTRY) as Country).languages.map { it.name }.toTypedArray().joinToString(separator = ", ")
                activity?.title = (it.get(BundleKeys.COUNTRY) as Country).name
            }
        }
    }
}