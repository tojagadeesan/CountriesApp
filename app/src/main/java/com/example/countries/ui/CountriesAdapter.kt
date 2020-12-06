package com.example.countries.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.countries.R
import com.example.countries.model.Country
import com.example.countries.utils.GlideApp
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class CountriesAdapter(
    val mContext: Context,
    var data: MutableList<Country>,
    spanCount: Int,
    var onItemClick: (country: Country) -> Unit
) :
    RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {
    private var widthPixels: Int
    private var originalData: MutableList<Country>

    init {

        val displayMetrics = DisplayMetrics()
        (mContext as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        widthPixels = displayMetrics.widthPixels / spanCount;
        originalData = data.toMutableList()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.shimmer_image.startShimmer()
        val name = data[position].flag.substring(
            data[position].flag.lastIndexOf("/") + 1,
            data[position].flag.lastIndexOf(".")
        )

        holder.itemView.layoutParams.width = widthPixels
        holder.itemView.imageView.layoutParams.width = widthPixels

        val file =
            File(ContextWrapper(mContext).getDir("images", Context.MODE_PRIVATE), "$name.jpg")
        if (file.exists()) {

            holder.itemView.imageView.setImageURI(Uri.parse((file.absoluteFile.toString())))
            holder.itemView.shimmer_image.hideShimmer()
        } else {
            GlideApp.with(mContext)
                .asDrawable()
                .load(data[position].flag)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        val bitmap = resource.toBitmap()
                        GlobalScope.launch(Dispatchers.Main) {
                            bitmap.apply {
                                val savedUri: Uri? = saveToInternalStorage(mContext, name)
                                holder.itemView.imageView.setImageURI(savedUri)
                                holder.itemView.shimmer_image.hideShimmer()
                            }
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })

        }
        holder.itemView.setOnClickListener {
            onItemClick(data[position])
        }
    }

    fun Bitmap.saveToInternalStorage(context: Context, name: String): Uri? {
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "$name.jpg")

        return try {
            val stream: OutputStream = FileOutputStream(file)
            compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            Uri.parse(file.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateList(list: List<Country>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun filter(text: String?) {
        if (text.isNullOrEmpty()) {
            updateList(originalData)
        } else {
            val temp: MutableList<Country> = ArrayList()
            for (d in originalData) {
                if (text.let { d.name.toUpperCase().contains(it.toUpperCase()) }!!) {
                    temp.add(d)
                }
            }
            updateList(temp)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {}
}