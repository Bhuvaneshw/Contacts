package com.acutecoder.contacts.contact

import android.util.ArraySet
import androidx.annotation.ColorInt

/**
 * Created by Bhuvaneshwaran
 * on 1:05 PM, 6/4/2023
 *
 * @author AcuteCoder
 */

data class ContactDetail(
    val id: String,
    val name: String,
    val numbers: ArraySet<String>,
    @ColorInt val color: Int
)