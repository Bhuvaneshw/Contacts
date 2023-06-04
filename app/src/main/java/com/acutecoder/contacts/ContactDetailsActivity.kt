package com.acutecoder.contacts

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.acutecoder.contacts.adapter.NumbersAdapter
import com.acutecoder.contacts.contact.Contact
import com.acutecoder.contacts.contact.ContactManager
import com.acutecoder.contacts.databinding.ActivityContactDetailsBinding
import com.acutecoder.contacts.databinding.ListContactDeatilsHeaderBinding

class ContactDetailsActivity : AppCompatActivity() {

    private lateinit var v: ActivityContactDetailsBinding

    companion object {
        var contact: Contact? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        v = ActivityContactDetailsBinding.inflate(layoutInflater)
        setContentView(v.root)

        if (contact == null) {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val contact = ContactManager.getContactDetail(this, contact)

        v.back.setOnClickListener {
            finish()
        }

        val header = ListContactDeatilsHeaderBinding.inflate(layoutInflater)
        header.name.text = contact.name
        header.number.text = contact.numbers.valueAt(0)
        header.photoText.apply {
            text =
                if (contact.name.isNotEmpty()) contact.name[0].toString() else ""
            backgroundTintList = ColorStateList.valueOf(contact.color)
        }

        val adapter = NumbersAdapter(this, contact) {
            startActivity(Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:$it") })
        }
        v.numbers.adapter = adapter
        v.numbers.addHeaderView(header.root)
        adapter.notifyDataSetChanged()
    }
}