package com.acutecoder.contacts.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.acutecoder.contacts.R
import com.acutecoder.contacts.contact.Contact
import com.acutecoder.contacts.contact.ContactList
import com.acutecoder.contacts.contact.Title
import com.acutecoder.contacts.databinding.ListContactBinding
import com.acutecoder.contacts.databinding.ListContactTitleBinding
import java.util.Random


/**
 * Created by Bhuvaneshwaran
 * on 11:56 PM, 6/2/2023
 *
 * @author AcuteCoder
 */

class ContactAdapter(private val activity: Activity, private val data: ContactList) :
    BaseAdapter() {

    override fun getCount() = data.size

    override fun getItem(position: Int): Contact = data[position]

    override fun getItemId(position: Int) = -1L

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val contact = data[position]
        val view =
            if (contact is Title) ListContactTitleBinding.inflate(activity.layoutInflater).root
            else ListContactBinding.inflate(activity.layoutInflater).root

        if (contact !is Title)
            view.findViewById<TextView>(R.id.name).text = contact.name

        view.findViewById<TextView>(R.id.icon).apply {
            text = contact.name.getInitial()
            if (contact !is Title)
                backgroundTintList = ColorStateList.valueOf(getRandomColor())
        }

        return view
    }

    private fun getRandomColor(): Int {
        val colors = arrayOf(
            Color.parseColor("#18acb6"),
            Color.parseColor("#f1a81a"),
            Color.parseColor("#f47d2e"),
            Color.parseColor("#2da2e1"),
            Color.parseColor("#886cc4"),
        )

        return colors[colors.indices.random()]
    }

    private fun IntRange.random() =
        if (this.last == 0) 0 else (Random().nextInt((endInclusive + 1) - start) + start)

    private fun String.getInitial() =
        if (length > 1) this.substring(0, 1) else if (isNotEmpty()) this[0].toString() else ""
}

