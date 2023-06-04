package com.acutecoder.contacts.adapter

import android.app.Activity
import android.util.ArraySet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.acutecoder.contacts.R
import com.acutecoder.contacts.contact.ContactDetail
import com.acutecoder.contacts.databinding.ListContactNumbersBinding


/**
 * Created by Bhuvaneshwaran
 * on 2:18 PM, 6/4/2023
 *
 * @author AcuteCoder
 */

class NumbersAdapter(
    private val activity: Activity,
    private val contact: ContactDetail,
    private val onClick: OnNumberClickListener? = null
) : BaseAdapter() {

    override fun getCount() = contact.numbers.size

    override fun getItem(position: Int) = contact.numbers[position]

    override fun getItemId(position: Int) = -1L

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: ListContactNumbersBinding.inflate(activity.layoutInflater).root
        view.apply {
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
            findViewById<TextView>(R.id.name).text = contact.numbers[position]
            setOnClickListener {
                onClick?.onClick(contact.numbers[position])
            }
        }

        return view
    }

    private fun isInCenter(position: Int) = position > 0 && position < contact.numbers.size - 1

    private fun isInTop(position: Int) = contact.numbers.size > 1 && position == 0

    private fun isInBottom(position: Int) =
        contact.numbers.size > 1 && position == contact.numbers.size - 1

    private operator fun ArraySet<String>.get(index: Int): String = valueAt(index)
}
