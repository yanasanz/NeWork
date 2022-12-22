package com.example.nework.view

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.nework.R

fun ImageView.load(url: String?, vararg transforms: BitmapTransformation = emptyArray()) {
    if (url == null) {
        setImageResource(R.drawable.ic_no_avatar_24)
    } else {
        Glide.with(this)
            .load(url)
            .error(R.drawable.ic_broken_image_24)
            .timeout(10_000)
            .transform(*transforms)
            .into(this)
    }
}

fun ImageView.loadCircleCrop(url: String?, vararg transforms: BitmapTransformation = emptyArray()) =
    url.let { load(it, CircleCrop(), *transforms) }