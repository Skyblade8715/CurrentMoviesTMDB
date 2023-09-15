package com.skycom.currentmoviestmdb.presentation.modules.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.skycom.currentmoviestmdb.databinding.RatingViewBinding
import kotlin.math.roundToInt

class RatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding: RatingViewBinding

    init {
        binding = RatingViewBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setProgress(ratingOutOfTen: Double) {

        val ratingPercentage = (ratingOutOfTen * 10).roundToInt()
        binding.progressItemIndicator.progress = ratingPercentage
        binding.progressItemText.text = "${ratingPercentage}%"

        when(ratingPercentage){
            in 0..50 -> {
                binding.progressItemIndicator
                    .setIndicatorColor(Color.parseColor("#FF1142"))
            }
            in 51..75 -> {
                binding.progressItemIndicator
                    .setIndicatorColor(Color.parseColor("#FAB927"))
            }
            else -> {
                binding.progressItemIndicator
                    .setIndicatorColor(Color.parseColor("#5AEB1B"))
            }
        }
    }
}