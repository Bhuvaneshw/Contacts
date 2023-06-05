package com.acutecoder.contacts.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
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
) : BaseAdapter() {

    private var filteredData = data

    override fun getCount() = filteredData.size

    override fun getItem(position: Int): Contact = filteredData[position]

    override fun getItemId(position: Int) = -1L

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val contact = filteredData[position]
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
                    onClick?.onClick(filteredData[position])
                }
            }

        return view
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()

                if (constraint.isNullOrEmpty()) {
                    results.values = data
                    results.count = data.size
                } else {
                    val filtered = ContactList()
                    for (contact in data) {
                        if (contact !is Title && contact.name.lowercase().contains(
                                constraint.toString().lowercase()
                            )
                        ) {
                            filtered.add(contact)
                        }
                    }
                    results.values = filtered
                    results.count = filtered.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredData = results.values as ContactList
                notifyDataSetChanged()
            }
        }
    }

    private fun isInCenter(position: Int) =
        position > 0 && filteredData[position - 1] !is Title && position < filteredData.size - 1 && filteredData[position + 1] !is Title

    private fun isInTop(position: Int) =
        position > 0 && filteredData[position - 1] is Title && position < filteredData.size - 1 && filteredData[position + 1] !is Title

    private fun isInBottom(position: Int) =
        position > 0 && filteredData[position - 1] !is Title && ((position < filteredData.size - 1 && filteredData[position + 1] is Title) || (position == filteredData.size - 1))

    private fun String.getInitial() =
        if (length > 1) this.substring(0, 1) else if (isNotEmpty()) this[0].toString() else ""
}

