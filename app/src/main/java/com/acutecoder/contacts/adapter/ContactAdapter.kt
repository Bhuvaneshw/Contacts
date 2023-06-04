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

/**
 * Created by Bhuvaneshwaran
 * on 11:56 PM, 6/2/2023
 *
 * @author AcuteCoder
 */

class ContactAdapter(
    private val activity: Activity,
    private val data: ContactList,
    private val onClick: OnClickListener? = null
) :
    BaseAdapter() {

    override fun getCount() = data.size

    override fun getItem(position: Int): Contact = data[position]

    override fun getItemId(position: Int) = -1L

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val contact = data[position]
        val view =
            if (contact is Title) ListContactTitleBinding.inflate(activity.layoutInflater).root.apply {
                findViewById<TextView>(R.id.icon).apply {
                    text = contact.name.getInitial()
                }
            }
            else ListContactBinding.inflate(activity.layoutInflater).root.apply {
                if (isInCenter(position)) {
                    setBackgroundResource(R.drawable.xml_click_bg_center)
                    findViewById<View>(R.id.topLine).visibility = View.VISIBLE
                } else if (isInTop(position)) {
                    setBackgroundResource(R.drawable.xml_click_bg_top)
                    findViewById<View>(R.id.topLine).visibility = View.GONE
                } else if (isInBottom(position)) {
                    setBackgroundResource(R.drawable.xml_click_bg_bottom)
                    findViewById<View>(R.id.topLine).visibility = View.VISIBLE
                } else {
                    setBackgroundResource(R.drawable.xml_click_bg_single)
                    findViewById<View>(R.id.topLine).visibility = View.GONE
                }
                findViewById<TextView>(R.id.name).text = contact.name
                findViewById<TextView>(R.id.iconText).apply {
                    text = contact.name.getInitial()
                    backgroundTintList = ColorStateList.valueOf(contact.color)
                }
                setOnClickListener {
                    onClick?.onClick(data[position])
                }
            }

        return view
    }

    private fun isInCenter(position: Int) =
        position > 0 && data[position - 1] !is Title && position < data.size - 1 && data[position + 1] !is Title

    private fun isInTop(position: Int) =
        position > 0 && data[position - 1] is Title && position < data.size - 1 && data[position + 1] !is Title

    private fun isInBottom(position: Int) =
        position > 0 && data[position - 1] !is Title && position < data.size - 1 && data[position + 1] is Title

    private fun String.getInitial() =
        if (length > 1) this.substring(0, 1) else if (isNotEmpty()) this[0].toString() else ""
}

