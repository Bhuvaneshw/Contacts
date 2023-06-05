package com.acutecoder.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.acutecoder.contacts.adapter.ContactAdapter
import com.acutecoder.contacts.contact.ContactManager
import com.acutecoder.contacts.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


/**
 * Created by Bhuvaneshwaran
 * https://acutecoder.ml
 * @author AcuteCoder
 */

class MainActivity : AppCompatActivity() {

    private lateinit var v: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
//        if (!checkUser()) return

        v = ActivityMainBinding.inflate(layoutInflater)
        setContentView(v.root)

        setSupportActionBar(v.toolbar)
        v.collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE)
        v.collapsingToolbar.setExpandedTitleColor(Color.WHITE)
        if (checkPermissions())
            init()
    }

    private fun checkUser(): Boolean {
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return false
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        checkUser()
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        val data = ContactManager.getContactList(this)
        data.updateContactSize()
        data.apply {
            sort()
            insertHeaders()
        }

        v.contactSize.text = "${data.contactSize} total Contacts"
        val adapter = ContactAdapter(this, data) {
            ContactDetailsActivity.contact = it
            startActivity(Intent(this, ContactDetailsActivity::class.java))
        }
        v.contactListView.adapter = adapter
        adapter.notifyDataSetChanged()

        v.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.getFilter().filter(text)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
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
                toast()
                finishAffinity()
            } else init()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.exportContacts -> {
//                val dialog = ProgressDialog(this)
//                dialog.setMessage("Syncing")
//                dialog.setCancelable(false)
//                dialog.show()
//                val ref = FirebaseDatabase.getInstance()
//                    .getReference(auth.currentUser!!.uid + "/contact")
//                FirebaseDatabase.getInstance()
//                    .getReference(auth.currentUser!!.uid + "/name")
//                    .setValue(auth.currentUser!!.displayName)
//                    .addOnCompleteListener {
//                        FirebaseDatabase.getInstance()
//                            .getReference(auth.currentUser!!.uid + "/email")
//                            .setValue(auth.currentUser!!.email)
//                            .addOnCompleteListener {
//                            }
//                    }
//                Thread {
//                    ref.setValue(ContactManager.getCompleteContactList(this))
//                        .addOnCompleteListener {
//                            runOnUiThread {
//                                dialog.dismiss()
//                                toast("Sync Completed")
//                            }
//                        }
//                }.start()
//            }

            R.id.search -> {
                v.searchCon.apply {
                    if (visibility == View.VISIBLE)
                        visibility = View.GONE
                    else
                        visibility = View.VISIBLE
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun toast(msg: String = "Permission Not Granted! Exiting...") =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}