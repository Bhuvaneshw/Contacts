package com.acutecoder.contacts.contact

import android.graphics.Color
import androidx.annotation.ColorInt

/**
 * Created by Bhuvaneshwaran
 * on 11:54 PM, 6/2/2023
 *
 * @author AcuteCoder
 */

open class Contact(val id: String, val name: String, @ColorInt val color: Int){
    companion object {

        fun getRandomColor(): Int {
            val colors = arrayOf(
                Color.parseColor("#18acb6"),
                Color.parseColor("#f1a81a"),
                Color.parseColor("#f47d2e"),
                Color.parseColor("#2da2e1"),
                Color.parseColor("#886cc4"),
            )

            return colors[colors.indices.random()]
        }

    }
}