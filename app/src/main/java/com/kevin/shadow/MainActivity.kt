package com.kevin.shadow

import android.graphics.Color
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kevin.shadow.databinding.ActivityMainBinding
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        loadCoverImg(
            binding.cover,
            "https://kimag.cdn.pandora.xiaomi.com/kv-cms-image/9C3AE14BDCAFD2569CF591BFD643E2E8_1550903326051.jpg"
        )
    }

    private fun loadCoverImg(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(RequestOptions().priority(Priority.HIGH))
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .apply(RequestOptions.centerCropTransform())
            .apply(
                RequestOptions.bitmapTransform(
                    RoundedCornersTransformation(
                        resources.getDimensionPixelOffset(R.dimen.radius),
                        0,
                        RoundedCornersTransformation.CornerType.ALL
                    )
                )
            )
            .into(imageView)
    }
}
