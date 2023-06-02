package com.acutecoder.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.acutecoder.contacts.adapter.ContactAdapter
import com.acutecoder.contacts.contact.ContactManager
import com.acutecoder.contacts.databinding.ActivityMainBinding


/**
 * Created by Bhuvaneshwaran
 * https://acutecoder.ml
 * @author AcuteCoder
 */

class MainActivity : AppCompatActivity() {

    private lateinit var v: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        v = ActivityMainBinding.inflate(layoutInflater)
        setContentView(v.root)

        setSupportActionBar(v.toolbar)
        v.collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE)
        v.collapsingToolbar.setExpandedTitleColor(Color.WHITE)
        if (checkPermissions())
            init()
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        val data = ContactManager.getContactList(contentResolver)
        data.updateContactSize()
        data.apply {
            sort()
            insertHeaders()
        }
        v.contactSize.text = "${data.contactSize} total Contacts"
        val adapter = ContactAdapter(this, data)
        v.contactListView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun checkPermissions(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        if (!result) requestPermissions(
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
            ), 1000
        )
        return result
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1000) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                toast("Permission Not Granted! Exiting...")
                finishAffinity()
            } else init()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exportContacts ->
                toast("Export Contacts")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}